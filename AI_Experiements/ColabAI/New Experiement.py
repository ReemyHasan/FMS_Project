import numpy as np
import pandas as pd
from imblearn.over_sampling import SMOTE
from imblearn.pipeline import Pipeline
from imblearn.under_sampling import RandomUnderSampler
from sklearn.ensemble import AdaBoostClassifier, BaggingClassifier, GradientBoostingClassifier
from sklearn.metrics import cohen_kappa_score, f1_score, recall_score, confusion_matrix, accuracy_score
from sklearn.model_selection import GridSearchCV, StratifiedKFold, train_test_split
from sklearn.tree import DecisionTreeClassifier

data = pd.read_csv('data_A1.csv')
X = data.drop('class', axis=1)
X = X.drop('timestamp', axis=1)
y = data['class']

X_smote, y_smote = X, y


smote = SMOTE(sampling_strategy='auto')
# X_smote, y_smote = smote.fit_resample(X, y)

X_train, X_test, y_train, y_test = train_test_split(X_smote, y_smote, test_size=0.3, random_state=42)
X_smote, y_smote = smote.fit_resample(X_train, y_train)
X_train, y_train = X_smote,y_smote
# X_test, y_test = X, y
base_classifier = DecisionTreeClassifier()
bagging_classifier = BaggingClassifier(base_classifier,n_estimators=150)
bagging_classifier.fit(X_train,y_train)
y_pred = bagging_classifier.predict(X_test)
########################
f1_scores = []
sensitivities = []
g_means = []
kappas = []
accuracies = []
f1_scores.append(f1_score(y_test, y_pred, average=None))
sensitivities.append(recall_score(y_test, y_pred, average=None))
g_means.append(
    np.sqrt(recall_score(y_test, y_pred, average=None) * recall_score(y_test, y_pred, average=None)).mean())
kappas.append(cohen_kappa_score(y_test, y_pred))
accuracies.append(accuracy_score(y_test, y_pred))
print("************\n")
cm = confusion_matrix(y_test, y_pred)
print(cm)
avg_f1_scores = np.mean(f1_scores, axis=0)
avg_sensitivities = np.mean(sensitivities, axis=0)
avg_g_mean = np.mean(g_means)
avg_kappa = np.mean(kappas)
avg_accuracy = np.mean(accuracies)
for i, class_label in enumerate(bagging_classifier.classes_):
    print(f"Metrics for class {class_label}:")
    print(f"F1-Score: {avg_f1_scores[i]}")
    print(f"Sensitivity: {avg_sensitivities[i]}")
    print()
print(f"Average G-Mean: {avg_g_mean}")
print(f"Average Kappa: {avg_kappa}")
print(f"Average Accuracy: {avg_accuracy}")

