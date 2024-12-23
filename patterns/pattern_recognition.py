# -*- coding: utf-8 -*-
"""pattern_recognition.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1w36SPsOWGgK0WlPPTbqArjWKCnb8-CLS
"""

!pip install anytree

import pdb

class Tree:
  #2 types of nodes: internal nodes - have a feature, leaves - have output_num, N, e, exceptions (no children)
  left = None
  right = None
  parent = None
  N = 0
  e = 0
  feature = None
  output_num = None
  data = None
  exceptions = []
  feature_name = None
  used_features = []


  def __init__(self, output_num, N, exceptions, feature, data):
    self.output_num = output_num
    self.N = N
    self.e = len(exceptions)
    self.exceptions = exceptions
    self.feature = feature
    self.data = data

  def set_left(self, node):
    self.left = node

  def set_right(self, node):
    self.right = node

  def set_parent(self, node):
    self.parent = node

  def set_N(self, N):
    self.N = N

  def set_feature(self, feature):
    self.feature = feature

  def set_output_num(self, output_num):
    self.output_num = output_num

  def set_exceptions(self, exceptions):
    self.exceptions = exceptions
    self.e = len(exceptions)

  def get_feature_name(self):
    return self.feature_name

  def set_feature_name(self, feature_name):
    self.feature_name = feature_name

  def get_feature(self):
    return self.feature

  def get_N(self):
    return self.N

  def get_e(self):
    return self.e

  def get_left(self):
    return self.left

  def get_right(self):
    return self.right

  def get_parent(self):
    return self.parent

  def get_exceptions(self):
    return self.exceptions

  def get_feature(self):
    return self.feature

  def get_output_num(self):
    return self.output_num

  def get_data(self):
    return self.data

  def add_data(self, new_data):
    self.data.append(new_data)

  def set_used_features(self, used_features):
    self.used_features = used_features

  def get_used_features(self):
    return self.used_features

  def __str__(self):
    return "output_feature: " + str(self.output_num) + "\nN: " + str(self.N) + "\ne: " + str(self.e) + "\nexceptions: " + str(self.exceptions) + "\nfeature: " + str(self.feature) + "\ndata: " + str(self.data)

import numpy as np
#import matplotlib.pylab as plt
#import pandas as pd
from collections import Counter
import math
import pdb
import copy

class Model:

  og_input = None
  og_output = None
  all_data = None
  num_features = 0
  root = None
  feature_names = []
  output_names = []

  def __init__(self, input, output, feature_names, output_names):
    self.og_input = input
    self.og_output = output
    self.num_features = len(input[0])
    self.all_data = [x + [y] for x, y in zip(input, output)]
    self.feature_names = feature_names
    self.output_names = output_names
    if len(input) == 1:
      self.root = Tree(0, 1, [], None, self.all_data)
    else:
      internal_node = self.create_branch(self.og_input, self.og_output, None, [], None)
      self.root = internal_node

  #must be greater than this number
  def threshold(self, num):
    if num <= 1 :
      return 0
    else:
      return num - num // math.log(num)

  def gen_node(self, input, output, used_features):
      num_data = len(input)
      #find max output
      c = Counter(output)
      t = 0
      actual_t = -1
      j = -1
      #increments colors
      while (actual_t < t):
        j += 1
        value, count = c.most_common()[j] #blue, 5
        t = self.threshold(count)

        original_list = [input[i] for i in range(num_data) if output[i] == value]
        majority_features = list(map(list, zip(*original_list)))

        #find max feature
        values = []
        counts = []
        for i in range(self.num_features):
          c = Counter(majority_features[i])
          values.append(c.most_common()[0][0])
          counts.append(c.most_common()[0][1])

        t2 = 0
        both_features = -1
        max_index = -1
        #increments features - find the max feature, see if it satisfies threshold
        while (both_features < t2 or max_index in used_features) and counts:
          if len(counts) == 0:
            return None, None, None, None, None
          max_count = max(counts) #how many is max
          max_index = counts.index(max_count) #which feature - odd, 1
          max_value = values[max_index] #is even or odd the max
          actual_t = max_count
          #verification
          both_features = 0
          f_count = 0
          exceptions = []
          for i in range(num_data):
            if input[i][max_index] == max_value:
              f_count += 1
              if output[i] == value:
                both_features += 1
              else:
                #include all features
                arr = copy.deepcopy(input[i])
                arr.append(output[i])
                exceptions.append(arr)
          t2 = self.threshold(f_count)
          del counts[max_index]
          del values[max_index] #increment
      e = f_count - both_features
      N = f_count
      yes = value
      feature_num = max_index
      feature = max_value
      return feature_num, feature, yes, N, exceptions

  def remove_data(self, input, output, feature_num, feature):
    #remove data
    new_input = []
    new_output = []
    pos_data = []
    neg_data = []
    for i in range(len(output)):
      arr = copy.deepcopy(input[i])
      arr.append(output[i])
      if input[i][feature_num] != feature:
        new_input.append(input[i])
        new_output.append(output[i])
        neg_data.append(arr)
      else:
        pos_data.append(arr)
    return new_input, new_output, pos_data, neg_data


  #returns , N, exceptions
  def other_branch(self, input, output):
    #find max output feature (color)
    if len(input) == 0:
      return 0, 0, input
    c = Counter(output)
    t = self.threshold(len(output))
    value, actual_t = c.most_common()[0]
    #if threshold is met
    if actual_t >= t:
      exceptions = []
      for i in range(len(input)):
        if output[i] != value:
          arr = copy.deepcopy(input[i])
          arr.append(output[i])
          exceptions.append(arr)
      N = len(input)
      return value, N, exceptions
    else:
      return None, 0, input

  def trace_up(self, input, output, parent, used_features, is_left):
    internal_data = [x + [y] for x, y in zip(input, output)]
    if parent is None:
      return Tree(None, len(input), internal_data, None, internal_data)
    parent_feature = parent.get_feature()
    grandparent = parent.get_parent()
    if grandparent is not None:
      grand_used_features = grandparent.get_used_features()
      new_used_features = grandparent.get_used_features() + [parent_feature]
      new_node = self.create_branch(input, output, grandparent, new_used_features)
      new_node.set_parent(grandparent)
      new_node.set_used_features(grand_used_features + [new_node.get_feature()])
      if is_left:
        grandparent.set_left(new_node)
      else:
        grandparent.set_right(new_node)
    else:
      new_used_features = [parent_feature]
      new_node = self.create_branch(input, output, None, new_used_features)
    return new_node


  def create_branch(self, input, output, parent, used_features, isLeft):
    feature_num, feature, yes, N, exceptions = self.gen_node(input, output, used_features)
    if feature_num is None:
      return self.trace_up(input, output, parent, used_features, isLeft)
    internal_data = [x + [y] for x, y in zip(input, output)]
    internal_node = Tree(None, 0, [], feature_num, internal_data)
    internal_node.set_feature_name(self.feature_names[feature_num])
    new_used_features = used_features + [feature_num]
    internal_node.set_used_features(new_used_features)
    new_input, new_output, pos_data, neg_data = self.remove_data(input, output, feature_num, feature)
    pos_leaf = Tree(yes, N, exceptions, None, pos_data)
    pos_leaf.set_feature_name(self.output_names[yes])
    internal_node.set_left(pos_leaf)
    pos_leaf.set_parent(internal_node)
    no, N, exceptions = self.other_branch(new_input, new_output)
    if no is None:
      neg_branch = self.create_branch(new_input, new_output, new_used_features)
      neg_branch.set_parent(internal_node)
      internal_node.set_right(neg_branch)
    else:
      neg_leaf = Tree(no, N, exceptions, None, neg_data)
      neg_leaf.set_feature_name(self.output_names[no])
      internal_node.set_right(neg_leaf)
      neg_leaf.set_parent(internal_node)
    return internal_node

  def get_root(self):
    return self.root

  def get_all_data(self):
    return self.all_data

  def get_feature_names(self):
    return self.feature_names

  def append_data(self, curr_node, data):
    curr_node.add_data(data)
    if curr_node.get_parent() is not None:
      self.append_data(curr_node.get_parent(), data)

  def add_item(self, item):
    node = self.find_node(item, self.root)
    self.append_data(node, item)
    #Case 1: New item is not exception
    new_N = node.get_N() + 1
    node.set_N(new_N)
    output_num = node.get_output_num()
    if item[-1] == output_num:
      print('Case 1')
      return self.root
    #then item must be an exception
    #Case 2: New item is exception but does not break threshold
    t = self.threshold(new_N)
    e = node.get_e()
    exceptions = node.get_exceptions()
    actual_t = e + 1
    if actual_t >= t:
      arr = copy.deepcopy(exceptions)
      arr.append(item)
      node.set_exceptions(arr)
      print('Case 2')
      return self.root
    #Case 3: New item is exception and breaks threshold
    print('Case 3')
    parent = node.get_parent()
    data = node.get_data()
    used_features = node.get_used_features()
    data_input = [row[:-1] for row in data]
    data_output = [row[-1] for row in data]
    isLeft = False
    if parent.get_left() == node:
      isleft = True
    internal_node = self.create_branch(data_input, data_output, parent, used_features, isLeft)
    return self.root

  #works for binary trees: recursive
  def find_node(self, item, node):
    if node.get_feature() == None:
      return node
    feature = node.get_feature()
    if item[feature] == 0:
      return self.find_node(item, node.get_left())
    else:
      return self.find_node(item, node.get_right())

import random

# Parameters
num_lists = 10  # Number of lists to generate
indices_per_list = 3  # Number of indices in each list

# Generate the lists of 0s and 1s
rand_input = [[random.choice([0, 1]) for _ in range(indices_per_list)] for _ in range(num_lists)]

# Generate 1000 random integers between 0 and 2 inclusive
rand_output = [random.randint(0, 2) for _ in range(num_lists)]

# Print the first 10 lists and first 10 random integers to check
print("First 10 lists of 0s and 1s:")
for i in range(10):
    print(rand_input[i])

print("\nFirst 10 random integers between 0 and 2:")
print(rand_output[:10])

feature_names = ['Shape', 'Odd/Even', 'Size', 'Color']
output_names = ['Red', 'Yellow', 'Blue']
m = Model(rand_input, rand_output, feature_names, output_names)

from anytree import Node, RenderTree
from anytree.exporter import DotExporter

# Define a custom class to store complex data in each node
class TreeNode(Node):
    def __init__(self, name, data=None, parent=None, children=None):
        super().__init__(name, parent, children)
        self.data = data

#m = Model(og_input, og_output)
node = m.get_root()

def create_tree_node(node, root, isPos):
  if node.get_feature() != None:
    return TreeNode(node.get_feature_name(), data={'Side': isPos, 'Type': 'Feature'}, parent = root)
  return TreeNode(node.get_feature_name(), data={'Side': isPos, 'N': node.get_N(), 'e': node.get_e(), 'exceptions': node.get_exceptions(), 'Type': 'Output'}, parent = root)


def create_tree(node, parent, isPos):
  if node is None:
    return None
  root = create_tree_node(node, parent, isPos)
  create_tree(node.get_left(), root, 'Left')
  create_tree(node.get_right(), root, 'Right')
  return root

root = create_tree(node, None, 'None')

# Print the tree structure
for pre, fill, node in RenderTree(root):
    print(f"{pre}{node.name}: {node.data}")

# Visualize the tree structure using graphviz
DotExporter(root).to_picture("tree.png")