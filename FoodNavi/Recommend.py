import sys
import os
import pandas as pd
import warnings; warnings.filterwarnings('ignore')
from datetime import datetime
from dateutil.parser import parse

def getHour(x):
    return x.hour

def getWeekday(x):
    return x.weekday()

def getDate(x):
    return x.date()

now = datetime.today()


# my_data_raw = pd.DataFrame({'useq':[1001], 'sex':['m'], 'age':[37], 'height':[181.0], 'weight':[100.0], 
#                            'no_egg':['n'], 'no_milk':['n'], 'no_bean':['n'], 'no_shellfish':['n'], 
#                            'purpose':['all'], 'diet_type':['all'], 'vegetarian':['0'], 
#                            'meal_type':['구분없음'], 'date':[now]})

args_arr = sys.argv[1].split(sep=',')
my_data_raw = pd.DataFrame({'useq':[int(args_arr[0])], 'sex':[args_arr[1]], 'age':[int(args_arr[2])], 
                            'height':[float(args_arr[3])], 'weight':[float(args_arr[4])], 'no_egg':[args_arr[5]], 
                            'no_milk':[args_arr[6]], 'no_bean':[args_arr[7]], 'no_shellfish':[args_arr[8]], 
                            'purpose':[args_arr[9]], 'diet_type':[args_arr[10]], 'vegetarian':[args_arr[11]], 
                            'meal_type':[args_arr[12]], 'date':[now]})

my_useq = my_data_raw['useq']
my_data = my_data_raw[['sex', 'age', 'height', 'weight', 'no_egg', 'no_milk', 'no_bean', 'no_shellfish', 'purpose', 'diet_type', 'vegetarian', 'meal_type', 'date']]
my_data['sex'] = my_data['sex'].apply(lambda x: 0 if x == 'm' else 1)
my_data['hour'] = my_data['date'].apply(getHour)
my_data['day'] = my_data['date'].apply(getWeekday)
my_data['date'] = my_data['date'].apply(getDate)
my_data['no_egg'] = my_data['no_egg'].apply(lambda x: 1 if x == 'y' else 0)
my_data['no_milk'] = my_data['no_milk'].apply(lambda x: 1 if x == 'y' else 0)
my_data['no_bean'] = my_data['no_bean'].apply(lambda x: 1 if x == 'y' else 0)
my_data['no_shellfish'] = my_data['no_shellfish'].apply(lambda x: 1 if x == 'y' else 0)
my_data['purpose'] = my_data['purpose'].apply(lambda x: 1 if x == 'diet' else 2 if x == 'bulkup' else 0)
my_data['diet_type'] = my_data['diet_type'].apply(lambda x: 1 if x == 'balance' else 2 if x == 'lowCarb' else 0)
my_data['vegetarian'] = my_data['vegetarian'].apply(lambda x: int(x))

tmp_data = my_data.copy()
for i in tmp_data.index:
    hour = tmp_data['hour'][i]
    if tmp_data['meal_type'][i] == 'morning':
        tmp_data['meal_type'][i] = 1
    elif tmp_data['meal_type'][i] == 'lunch':
        tmp_data['meal_type'][i] = 2
    elif tmp_data['meal_type'][i] == 'dinner':
        tmp_data['meal_type'][i] = 3
    elif tmp_data['meal_type'][i] == 'snack':
        tmp_data['meal_type'][i] = 4
    elif 5 <= tmp_data['hour'][i] <= 8:
        tmp_data['meal_type'][i] = 1
    elif 11 <= tmp_data['hour'][i] <= 14:
        tmp_data['meal_type'][i] = 2
    elif 17 <= tmp_data['hour'][i] <= 20:
        tmp_data['meal_type'][i] = 3
    else:
        tmp_data['meal_type'][i] = 4
my_data=tmp_data.copy()



my_data2 = my_data.copy()

history_data_raw = pd.read_csv('History.csv', encoding='utf-8')
history_data_raw.rename(columns=({'served_date':'date'}), inplace=True)
history_data = history_data_raw.copy()

food_data_in_history_raw = history_data[['food_kcal', 'food_carb', 'food_prt', 'food_fat']]

user_data_raw = history_data[['useq', 'sex', 'age', 'height', 'weight', 'no_egg', 'no_milk', 'no_bean', 'no_shellfish', 'purpose', 'diet_type', 'vegetarian', 'meal_type', 'date']]
user_data_raw.columns = my_data_raw.columns

user_data = user_data_raw.copy()
user_data.set_index('useq', inplace=True)

try:
    user_data.drop(my_useq, inplace=True)
except:
    pass

user_data.reset_index(inplace=True)
user_data.drop('useq', axis=1, inplace=True)

user_data['sex'] = user_data['sex'].apply(lambda x: 0 if x == 'm' else 1)
user_data['date'] = user_data['date'].apply(parse)
user_data['hour'] = user_data['date'].apply(getHour)
user_data['day'] = user_data['date'].apply(getWeekday)
user_data['date'] = user_data['date'].apply(getDate)
user_data['no_egg'] = user_data['no_egg'].apply(lambda x: 1 if x == 'y' else 0)
user_data['no_milk'] = user_data['no_milk'].apply(lambda x: 1 if x == 'y' else 0)
user_data['no_bean'] = user_data['no_bean'].apply(lambda x: 1 if x == 'y' else 0)
user_data['no_shellfish'] = user_data['no_shellfish'].apply(lambda x: 1 if x == 'y' else 0)
user_data['purpose'] = user_data['purpose'].apply(lambda x: 1 if x == 'diet' else 2 if x == 'bulkup' else 0)
user_data['diet_type'] = user_data['diet_type'].apply(lambda x: 1 if x == 'balance' else 2 if x == 'lowCarb' else 0)
user_data['vegetarian'] = user_data['vegetarian'].apply(lambda x: int(x))

tmp_data = user_data.copy()
for i in tmp_data.index:
    hour = tmp_data['hour'][i]
    if tmp_data['meal_type'][i] == 'morning':
        tmp_data['meal_type'][i] = 1
    elif tmp_data['meal_type'][i] == 'lunch':
        tmp_data['meal_type'][i] = 2
    elif tmp_data['meal_type'][i] == 'dinner':
        tmp_data['meal_type'][i] = 3
    elif tmp_data['meal_type'][i] == 'snack':
        tmp_data['meal_type'][i] = 4
    elif 5 <= tmp_data['hour'][i] <= 8:
        tmp_data['meal_type'][i] = 1
    elif 11 <= tmp_data['hour'][i] <= 14:
        tmp_data['meal_type'][i] = 2
    elif 17 <= tmp_data['hour'][i] <= 20:
        tmp_data['meal_type'][i] = 3
    else:
        tmp_data['meal_type'][i] = 4
user_data=tmp_data.copy()

user_data_raw['date_diff'] = user_data['date'].apply(lambda x: (my_data2.iloc[0]['date'] - x).days)
my_data2.drop('date', axis=1, inplace=True)
user_data.drop('date', axis=1, inplace=True)



# 성별과 나이대를 좀 더 강하게 적용하는 방법을 추가해야 함

# 표준화된 데이터를 사용
user_mean = user_data.mean()
user_std = user_data.std()

my_data_nor = my_data2.copy()
for col in my_data2.columns:
    if user_std[col] == 0:
        my_data_nor[col] = 0
    else:
        my_data_nor[col] = abs(my_data[col]-user_mean[col]) / user_std[col]

user_data_nor = user_data.copy()
for col in user_data.columns:
    if user_std[col] == 0:
        user_data_nor[col] = 0
    else:
        user_data_nor[col] = abs(user_data_nor[col] - user_mean[col])
        user_data_nor[col] /= user_std[col]

from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

sim_user_data = cosine_similarity(my_data_nor, user_data_nor)
sim_user_data_sorted_index = sim_user_data[0].argsort()[::-1]

food_feature_list_raw = food_data_in_history_raw.loc[sim_user_data_sorted_index[:]]
food_feature_list = food_feature_list_raw.copy()
food_feature_list_raw['date_diff'] = user_data_raw.loc[sim_user_data_sorted_index[:]]['date_diff']

user_score_list = []
for i in range(len(user_data_nor)):
    user_score_list.append(sim_user_data[0][sim_user_data_sorted_index[i]])
food_feature_score = pd.DataFrame({'score':user_score_list})

food_feature_list['user_score'] = user_score_list


from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

food_data_raw = pd.read_csv('Food.csv', encoding='utf-8')
food_data_raw.dropna(inplace=True)
food_data_mean = food_data_raw[['kcal', 'carb', 'prt', 'fat']].mean()
food_data_std = food_data_raw[['kcal', 'carb', 'prt', 'fat']].std()

cols = ['kcal', 'carb', 'prt', 'fat']
food_data_for_test = food_data_raw[cols]

food_feature_list.columns = ['kcal', 'carb', 'prt', 'fat', 'score']
food_feature_list.reset_index(inplace=True)
food_feature_list.drop('index', axis= 1, inplace=True)

food_feature_list_for_test = food_feature_list[cols]

for n in cols:
    if food_data_std[n] == 0:
        food_feature_list_for_test[n] = 0
        food_data_for_test[n] = 0
    else:
        food_feature_list_for_test[n] = abs(food_feature_list_for_test[n] - food_data_mean[n]) / food_data_std[n]
        food_data_for_test[n] = abs(food_data_for_test[n] - food_data_mean[n]) / food_data_std[n]
        
# 필터링 적용 준비
food_filter_raw = pd.read_csv('tmp_filtered.csv', encoding='utf-8')
food_filter = food_filter_raw['fseq']
food_filter = food_filter.values

fseq_filtered = []
score_filtered = []
food_filter = list(food_filter)
food_filter_index = [i-1 for i in food_filter]

final_food_list = []
final_food_count = []
tmp_score_list = []
tmp_adjusted_score_list = []
final_food_score_list = []

for i in range(len(food_feature_list_for_test)):
    sim_food_data = cosine_similarity(food_feature_list_for_test.iloc[i:i+1], food_data_for_test)
    sim_food_data_filtered = sim_food_data[0][[food_filter_index]]
    sim_food_data_sorted_index = sim_food_data_filtered[0].argsort()[::-1]
    for j in range(len(sim_food_data_filtered[0])):
        idx = sim_food_data_sorted_index[j]
        fseq = food_filter[idx]
        tmp_score = sim_food_data_filtered[0][idx]
        date_diff = food_feature_list_raw.iloc[i]['date_diff']
        decrease_ratio = 1-0.01*date_diff
        if decrease_ratio <= 0:
            decrease_ratio = 0
        if fseq not in final_food_list:
            final_food_list.append(fseq)
            final_food_count.append(1)
            tmp_score_list.append(tmp_score)
            tmp_adjusted_score_list.append(tmp_score*decrease_ratio)
            final_food_score_list.append(tmp_score*decrease_ratio*food_feature_list.iloc[i]['score'])
        else:
            idx2 = final_food_list.index(fseq)
            final_food_count[idx2] += 1
            if final_food_score_list[idx2] < tmp_score*decrease_ratio*food_feature_list.iloc[i]['score']:
                tmp_score_list[idx2] = tmp_score
                tmp_adjusted_score_list[idx2] = tmp_score*decrease_ratio
                final_food_score_list[idx2] = tmp_score*decrease_ratio*food_feature_list.iloc[i]['score']

all_score_view = pd.DataFrame({'fseq':final_food_list, 'count':final_food_count, 'tmp_score':tmp_score_list, 'tmp_adjusted_score':tmp_adjusted_score_list, 'total_score':final_food_score_list})

final_food_recommend_score_list = []
for i in range(len(final_food_list)):
    count = final_food_count[i]
    if count >= 10:
        count = 10
    final_food_recommend_score_list.append(final_food_score_list[i]*(0.9+0.01*count))
    
final_result = pd.DataFrame({'fseq':final_food_list, 'score':final_food_recommend_score_list})
final_result.set_index('fseq', inplace=True)


final_result.sort_values(by='score', ascending=False, inplace=True)
tmp_csv = 'tmp_recommendList.csv'
final_result.to_csv(tmp_csv, sep=',', encoding='utf-8')

while True:
    if os.path.exists(tmp_csv):
        break
        
print('success')