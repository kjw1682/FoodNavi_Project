import sys
import pandas as pd
import os

new_data = {'fseq': [sys.argv[1]], 'name': [sys.argv[2]], 'kcal': [sys.argv[3]], 'carb': [sys.argv[4]], 'prt': [sys.argv[5]], 'fat': [sys.argv[6]]}
new_df = pd.DataFrame(new_data)
new_df.set_index('fseq', inplace=True)

if not os.path.exists('food_data.csv'):
    new_df.to_csv('food_data.csv', encoding='cp949')
    print('새 CSV파일 생성 완료.')
else:
    df = pd.read_csv('food_data.csv', encoding='cp949')
    df.set_index('fseq', inplace=True)
    df.loc[int(new_df.index[0])] = [sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6]]
    df.to_csv('food_data.csv', encoding='cp949')
    print('데이터 저장,수정 완료.')