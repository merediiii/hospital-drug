# coding: utf-8
import tensorflow as tf
import os
import numpy as np
import sys
import re
#from PIL import Image
import matplotlib.pyplot as plt

lines = tf.gfile.GFile('D:\workspace\Idea\db-hospital-drug\src\main\\resources\py\\retrained_labels.txt').readlines()
uid_to_human = {}
#一行一行读取数据
for uid,line in enumerate(lines) :
    #去掉换行符
    line=line.strip('\n')
    uid_to_human[uid] = line

def id_to_string(node_id):
    if node_id not in uid_to_human:
        return ''
    return uid_to_human[node_id]

with tf.gfile.FastGFile('D:\workspace\Idea\db-hospital-drug\src\main\\resources\py\\retrained_graph.pb', 'rb') as f:
    graph_def = tf.GraphDef()
    graph_def.ParseFromString(f.read())
    tf.import_graph_def(graph_def, name='')

def predict(img):
    with tf.Session() as sess:
        softmax_tensor = sess.graph.get_tensor_by_name('final_result:0')
        #遍历目录

        #载入图片
        image_data = tf.gfile.FastGFile(img, 'rb').read()
        predictions = sess.run(softmax_tensor,{'DecodeJpeg/contents:0': image_data})#图片格式是jpg格式
        predictions = np.squeeze(predictions)#把结果转为1维数据
        top_k = predictions.argsort()[::-1]
        for node_id in top_k:
            #获取分类名称
            human_string = id_to_string(node_id)
            #获取该分类的置信度
            score = predictions[node_id]
            print('%s (score=%.5f)' % (human_string, score))
        print()

predict(sys.argv[1])