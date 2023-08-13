import joblib
import numpy as np
import pandas as pd
from flask import Flask, request
import py_eureka_client.eureka_client as eureka_client
from flask_cors import cross_origin
from sklearn.ensemble import GradientBoostingClassifier
from elasticsearch import Elasticsearch, helpers
from pandas import json_normalize
import atexit
from sklearn.metrics import cohen_kappa_score, accuracy_score, f1_score, recall_score, confusion_matrix
def cleanup():
    eureka_client.stop()
    print("Cleaning up resources...")
atexit.register(cleanup)
rest_port = 8050
eureka_client.init(
           eureka_server="http://192.168.27.227:8761/eureka",
           app_name="ML-MODEL-SERVICE",
           instance_host="192.168.24.47",
           instance_port=8050)
es = Elasticsearch("http://172.29.3.220:9200")
order = ['ICMP_ping', 'ICMP_loss', 'ICMP_response_time',
       'Device_uptime', 'SNMP_availability', 'range',
       'EX_Inbound_packets_discarded', 'IN_Inbound_packets_discarded',
       'P_Inbound_packets_discarded', 'EX_Outbound_packets_discarded',
       'IN_Outbound_packets_discarded', 'P_Outbound_packets_discarded',
       'EX_Inbound_packets_with_errors', 'IN_Inbound_packets_with_errors',
       'P_Inbound_packets_with_errors', 'EX_Outbound_packets_with_errors',
       'IN_Outbound_packets_with_errors', 'P_Outbound_packets_with_errors',
       'EX_Bits_sent', 'IN_Bits_sent', 'P_Bits_sent', 'EX_Bits_received',
       'IN_Bits_received', 'P_Bits_received', 'EX_Speed', 'IN_Speed',
       'P_Speed', 'EX_Interface_type', 'IN_Interface_type', 'P_Interface_type',
       'EX_Operational_status', 'IN_Operational_status',
       'P_Operational_status']

def getAllfromIndex(index_name):
    query = {"query": {"match_all": {}}}
    response = es.search(index=index_name, body=query, size=5)
    data = helpers.scan(es,
                        query=query,
                        index=index_name
                        )
    num = 0
    lst = []
    for doc in data:
        lst.append(doc['_source'])
        # if num == 10:
        #     break
        num += 1
        if num == 12000:
            break
    df2 = json_normalize(lst)
    return df2

filename = "my_model.joblib"
model = GradientBoostingClassifier()
# load model
model = joblib.load(filename)

app = Flask(__name__)

@app.route("/model/predict", methods=['POST'])
def predictClass():
    # time.sleep(60)
    print(request.json)
    data = request.json
    df = pd.DataFrame.from_dict(data,orient='index').T
    df = df.drop('timestamp',axis=1)
    df.reindex(order)
    response = model.predict(df)
    print(response)
    return response[0]

@app.route("/model/train", methods=['GET'])
@cross_origin()
def trainModel():
    index_name = "knowledge-base"
    df2 = getAllfromIndex(index_name)
    X = df2.drop('class', axis=1)
    X = X.drop('timestamp', axis=1)
    X = X.reindex(order,axis="columns")
    y = df2['class']
    model.fit(X,y)
    return "done"

def calcMetrics(model,X_test,y_test):
    # Initialize lists to store the metrics for each class
    f1_scores = []
    sensitivities = []
    g_means = []
    kappas = []
    # Perform cross-validation

    # Make predictions on the test set
    y_pred = model.predict(X_test)

    # Calculate the metrics for each class
    f1_scores.append(f1_score(y_test, y_pred, average=None))
    sensitivities.append(recall_score(y_test, y_pred, average=None))
    g_means.append(
        np.sqrt(recall_score(y_test, y_pred, average=None) * recall_score(y_test, y_pred, average=None)).mean())
    kappas.append(cohen_kappa_score(y_test, y_pred))

    # Calculate the average metrics across all folds
    avg_f1_scores = np.mean(f1_scores, axis=0)
    avg_sensitivities = np.mean(sensitivities, axis=0)
    avg_g_mean = np.mean(g_means)
    avg_kappa = np.mean(kappas)
    ret = ""
    # Print the metrics for each class
    for i, class_label in enumerate(model.classes_):
        ret += f"Metrics for class {class_label}:\n"
        print(f"Metrics for class {class_label}:")
        ret += f"F1-Score: {avg_f1_scores[i]}\n"
        print(f"F1-Score: {avg_f1_scores[i]}")
        ret += f"Sensitivity: {avg_sensitivities[i]}"
        ret += "\n"
        print(f"Sensitivity: {avg_sensitivities[i]}")
        print()

    # Print the average metrics
    ret += f"Average G-Mean: {avg_g_mean}\n"
    print(f"Average G-Mean: {avg_g_mean}")
    ret += f"Average Kappa: {avg_kappa}\n"
    print(f"Average Kappa: {avg_kappa}")
    labels = ["A","D","NE"]
    # Calculate confusion matrix
    cm = confusion_matrix(y_test, y_pred,labels=labels)
    # Get the number of classes
    num_classes = len(labels)
    # Create a string representation of the confusion matrix
    cm_string = "Confusion Matrix:\n"
    for i in range(num_classes):
        cm_string += f"Class {labels[i]}:\t"
        for j in range(num_classes):
            cm_string += f"{cm[i, j]}\t"
        cm_string += "\n"
    # Print the confusion matrix
    print(cm_string)
    ret += "\n" + cm_string + "\n"
    return ret

@app.route("/model/metrics", methods=['GET'])
@cross_origin()
def returnMetrics():
    index_name = "knowledge-base"
    df2 = getAllfromIndex(index_name)
    X = df2.drop('class', axis=1)
    X = X.drop('timestamp', axis=1)
    print(X)
    X = X.reindex(order,axis="columns")
    print(X)
    y = df2['class']
    ret = calcMetrics(model,X,y)
    return ret


if __name__ == "__main__":
    app.run(host='0.0.0.0',port=rest_port)

