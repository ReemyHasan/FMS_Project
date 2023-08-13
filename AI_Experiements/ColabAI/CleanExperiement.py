import numpy as np
import pandas as pd
from imblearn.over_sampling import SMOTE
from imblearn.pipeline import Pipeline
from imblearn.under_sampling import RandomUnderSampler
from sklearn.ensemble import AdaBoostClassifier, BaggingClassifier, GradientBoostingClassifier
from sklearn.metrics import cohen_kappa_score, f1_score, recall_score, confusion_matrix
from sklearn.model_selection import GridSearchCV, StratifiedKFold
from sklearn.tree import DecisionTreeClassifier


def kappa_scorer(estimator, X, y):
  y_pred = estimator.predict(X)
  return cohen_kappa_score(y, y_pred)

def BestParams(model,param_grid,X,y):
    grid_search = GridSearchCV(model, param_grid, scoring=kappa_scorer, cv=10)
    grid_search.fit(X, y)
    print("Best Parameters: ", grid_search.best_params_)
    print("Best Kappa Score: ", grid_search.best_score_)
    return grid_search.best_params_

def Metrics(model,X,y):
    cv = StratifiedKFold(n_splits=10, shuffle=False, random_state=None)
    f1_scores = []
    sensitivities = []
    g_means = []
    kappas = []
    # Perform cross-validation
    for train_index, test_index in cv.split(X, y):
        trains = [True if i in train_index else False for i in range(len(X))]
        tests = [True if i in test_index else False for i in range(len(X))]
        train_index = trains
        test_index = tests
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        model.fit(X_train, y_train)
        y_pred = model.predict(X_test)
        # Calculate the metrics for each class
        f1_scores.append(f1_score(y_test, y_pred, average=None))
        sensitivities.append(recall_score(y_test, y_pred, average=None))
        g_means.append(
            np.sqrt(recall_score(y_test, y_pred, average=None) * recall_score(y_test, y_pred, average=None)).mean())
        kappas.append(cohen_kappa_score(y_test, y_pred))

    avg_f1_scores = np.mean(f1_scores, axis=0)
    avg_sensitivities = np.mean(sensitivities, axis=0)
    avg_g_mean = np.mean(g_means)
    avg_kappa = np.mean(kappas)
    for i, class_label in enumerate(model.classes_):
        print(f"Metrics for class {class_label}:")
        print(f"F1-Score: {avg_f1_scores[i]}")
        print(f"Sensitivity: {avg_sensitivities[i]}")
        print()
    print(f"Average G-Mean: {avg_g_mean}")
    print(f"Average Kappa: {avg_kappa}")
    cm = confusion_matrix(y_test, y_pred)
    print(cm)

datasets = ["data_F1.csv","data_F2.csv","data_A1.csv","data_A2.csv"]
for s in datasets:
    data = pd.read_csv(s)
    X = data.drop('class', axis=1)
    X = X.drop('timestamp', axis=1)
    y = data['class']
    over = SMOTE(sampling_strategy="auto")
    under = RandomUnderSampler(sampling_strategy={"NE":round(0.7*len(X))})
    # steps = [('u', under),('o', over)]
    steps = [('o', over)]
    pipeline = Pipeline(steps=steps)
    X, y = pipeline.fit_resample(X, y)
    print(len(X))
    base_classifier = DecisionTreeClassifier()
    bagging_classifier = BaggingClassifier(base_classifier)
    gradient = GradientBoostingClassifier()
    adaboost = AdaBoostClassifier()

    n_estimators = [10,50,100,150,200]
    learning_rate = 0.1
    print("*** Result for file: ", s,"\n")
    for i in range(len(n_estimators)):
    #     print("Bagging Results for a number of estimators ",n_estimators[i])
    # # best = BestParams(bagging_classifier,param_grid_bag,X,y)
    #     bagging_classifier = BaggingClassifier(base_classifier,n_estimators=n_estimators[i])
    #     Metrics(bagging_classifier,X,y)
    #     print("********\n")
    #     print("AdaBoost Results for a number of estimators",n_estimators[i])
    #     # best = BestParams(adaboost, param_grid_boost,X,y)
    #     adaboost = AdaBoostClassifier(n_estimators=n_estimators[i],learning_rate=learning_rate)
    #     Metrics(adaboost, X, y)
        print("********\n")
        print("GradientBoost Results for a number of estimators", n_estimators[i])
        gradient = GradientBoostingClassifier(n_estimators=n_estimators[i],learning_rate=learning_rate)
        Metrics(gradient,X,y)
        print("********\n")


