# -*- coding: utf-8 -*-
"""
Created on Tue Jan 18 06:20:27 2022

@author: CarlesBouJ
"""
from scipy.stats import shapiro, levene, wilcoxon, ttest_ind
import pandas as pd
import numpy as np

def stat_test_equal(a, b, confidence_level = 0.05):
    _, p_a = shapiro(a)
    _, p_b = shapiro(b)
    
    test = ''
    if p_a >= confidence_level and p_b >= confidence_level: # las dos distribuciones son normales
        _, p = levene(a, b)
        if p >= confidence_level: # varianzas homogéneas
            _, p = ttest_ind(a, b, equal_var=True)
            test = 'Student'
        else:
            _, p = ttest_ind(a, b, equal_var=False)
            test = 'Welch'
    else:
        _, p = wilcoxon(a, b)
        test = 'Wilcoxon'
        
    print("Testname = ", test, "p=", format(p, '.2e'), "test=", p >= confidence_level)
    return p >= confidence_level, p, test

   
conf_level = 0.01

df = pd.read_csv('results.csv', delimiter=';')

df_MyNSGAII = df[df['Algoritmo'] == 'MyNSGAII']
df_NSGAII = df[df['Algoritmo'] == 'NSGAII']

pr = pd.DataFrame(columns = {'problema', 'indicador', 'algoritmo', 'mean'})
prtest = pd.DataFrame(columns = {'problema', 'indicador', 'stat_test', 'stat_test_val'}, dtype = str)

for indicator in ['hv', 'igdp', 'spread']:
#for indicator in ['hv', 'igd', 'igdp', 'spread']:
#for indicator in ['hv', 'spread']:
    name_algo = ''
    name_prob = ''
    a = []
    a = {}
    row = {} 
    meanA = 'Nada'
    meanB = 0
    meanAB = {}

    for name, group in df.groupby(['Problema', 'Algoritmo']):
        #print('Name=', name)
        if name_prob == '':
            name_prob = name[0]
            print(name_prob, indicator)
           
        elif name[0] != name_prob:
            test, p, testname = stat_test_equal(a['NSGAII'], a['MyNSGAII'], conf_level)
            if test:
                print('   Equal algorithms (p=', p, ')', sep='')
                testp = '='
            else:
                if indicator in ['hv']:
                    #if meanA <= meanB:
                    if meanAB['NSGAII'] < meanAB['MyNSGAII']:
                        best = 1
                        print("+ en", name_prob, "con", indicator, "meanA=", meanA, "meanB=", meanB)
                        testp = '+'
                    else:
                        best = 0
                        testp = '-'
                else:
                    #if meanA < meanB:
                    if meanAB['NSGAII'] < meanAB['MyNSGAII']:
                        best = 0
                        testp = '-'
                    else:
                        best = 1
                        testp = '+'
                
                meanA = 'Nada'
                meanAB = {}
                
                if testp == '-':
                    best_algo = 'NSGAII'
                else:
                    best_algo = 'MyNSGAII'
                
                print('   Best algorithm ', best_algo, ' (p=', p, ')', sep='')

            #print('   Best algorithm', df['Algoritmo'].drop_duplicates().values[best])
               
            prtestrow = {'problema': name_prob,
                         'indicador': indicator,
                         'stat_test': testp,
                         'stat_test_val': format(p, '.2e'),
                         'stat_test_name': testname}
            prtest = prtest.append(prtestrow, ignore_index=True)
            #row['stat_test'] = testp
            #pr = pr.append(row, ignore_index=True)

            a = {}

            name_prob = name[0]
            print()
            print(name_prob, indicator)
            
        #print(indicator, 'count=', group[indicator].count())
        #print(group.describe())
        #mean1 = group[indicator].mean()
        mean1 = group[indicator].replace(0, np.nan).mean(skipna=True)
        if np.isnan(mean1):
          mean1 = 0
        std1 = group[indicator].std()
        max1 = group[indicator].max()
        min1 = group[indicator].min()
        median1 = group[indicator].median()
        q1 = group[indicator].quantile(0.25)
        q2 = group[indicator].quantile(0.50)
        q3 = group[indicator].quantile(0.75)
        print('  ', name[1], 
              ' mean= ', format(mean1, '.5e'), 
              '(', format(std1, '.5e'), ')',
              ' max= ', format(max1, '.5e'),
              ' min= ', format(min1, '.5e'),
              ' median= ', format(median1, '.5e'),
              ' iqr= ', format(q3 - q1, '.5e'), sep='')
        
        row = {'problema': name_prob, 
               'indicador': indicator, 
               'algoritmo': name[1],
               'mean': format(mean1, '.5e') + 
               '(' + format(std1, '.5e') + ')',
               'meanval': format(mean1, '.6f')}
        
        pr = pr.append(row, ignore_index=True)
        
        if name[1] == 'NSGAII':
            meanAB['NSGAII'] = mean1
        else:
            meanAB['MyNSGAII'] = mean1
 
        '''
        if meanA == 'Nada':
            meanA = mean1
        else:
            meanB = mean1
        '''
        
        a[name[1]] = group[indicator].tolist()
            
        #a.append(group[indicator].tolist())
        
    test, p, testname = stat_test_equal(a['NSGAII'], a['MyNSGAII'], conf_level)
    if test:
        print('   Equal algorithms (p=', p, ')', sep='')
        testp = '='
    else:
        if indicator in ['hv']:
            #if meanA <= meanB:
            if meanAB['NSGAII'] < meanAB['MyNSGAII']:
                best = 1
                testp = '+'
            else:
                best = 0
                testp = '-'
        else:
            if meanAB['NSGAII'] < meanAB['MyNSGAII']:
            #if meanA < meanB:
                best = 0
                testp = '-'
            else:
                best = 1
                testp = '+'
                
        if testp == '-':
            best_algo = 'NSGAII'
        else:
            best_algo = 'MyNSGAII'
            
        #print('   Best algorithm', df['Algoritmo'].drop_duplicates().values[best])
        print('   AABest algorithm ', best_algo, ' (p=', p, ')', sep='')

    prtestrow = {'problema': name_prob,
                 'indicador': indicator,
                 'stat_test': testp,
                 'stat_test_val': format(p, '.2e'),
                 'stat_test_name': testname}
                 #'stat_test_val': format(p, '.5f')}
    
    prtest = prtest.append(prtestrow, ignore_index=True)
    
    #row['stat_test'] = testp
    #pr = pr.append(row, ignore_index=True)
        
    print()
    

with open('new_results.csv', 'w') as f, open('new_results2.csv', 'w') as f2:
        for probname, problema in pr.groupby(['problema']):
            st = ''        
            st2 = ''
            for indicador, gr in problema.groupby(['indicador']):
                st = st + probname + ';'
                st2 = st2 + probname + ';'
                mean_NSGAII = gr[gr['algoritmo'] == 'NSGAII']['mean'].values[0]
                mean_MyNSGAII = gr[gr['algoritmo'] == 'MyNSGAII']['mean'].values[0]
                meanval_NSGAII = gr[gr['algoritmo'] == 'NSGAII']['meanval'].values[0]
                meanval_MyNSGAII = gr[gr['algoritmo'] == 'MyNSGAII']['meanval'].values[0]

                stat_test_val = prtest[(prtest['problema'] == probname) & (prtest['indicador'] == indicador)]
                stat_test_val = stat_test_val['stat_test_val'].values[0]
                
                st += mean_NSGAII + ';'
                st += mean_MyNSGAII + ';'
                
                st2 += meanval_NSGAII + ';'
                st2 += meanval_MyNSGAII + ';'

                testname = prtest[(prtest['problema'] == probname) & (prtest['indicador'] == indicador)]
                testname = testname['stat_test_name'].values[0]
                
                test = prtest[(prtest['problema'] == probname) & (prtest['indicador'] == indicador)]
                test = test['stat_test'].values[0]
            
                st += test + ';' + stat_test_val + ';' + testname + ';;'
                st2 += test + ';' + stat_test_val + ';;'
            
            st = st + '\n'
            st2 = st2 + '\n'
            
            print(st)
                    
            f.write(st)
            f2.write(st2)
        
    
        