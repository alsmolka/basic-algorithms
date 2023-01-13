import networkx as nx
import random
import matplotlib.pyplot as plt
import heapq
import numpy as np
import os
import pandas as pd
import itertools

from timeit import default_timer as timer

#https://www.algorithms-and-technologies.com/dijkstra/python
#https://bradfieldcs.com/algos/graphs/dijkstras-algorithm/
"""
#https://stackoverflow.com/questions/73682082/heap-dijkstra-implementation-is-slower-than-naive-dijsktra-implementation
#https://gist.github.com/jasonhuh/957a75fc498a18a398069efb5a325db1
from heapq import heappush, heappop
from collections import defaultdict

def FastDijkstra(vertices, start_point, lengths):
    # Create a dictionary for the edges, keyed by source node
    edges = defaultdict(list)
    for key, length in lengths.items():
        x, y = key.split("-")
        edges[x].append((length, y))

    heap = [(0, start_point)]
    shortest_paths = {}
    while heap:
        cost, x = heappop(heap)
        if x in shortest_paths:
            continue  # this vertex had already been on the heap before
        shortest_paths[x] = cost
        for length, y in edges[x]:
            if y not in shortest_paths:
                heappush(heap, (cost + length, y))
    return shortest_paths
"""


def calculate_heap(graph, starting_vertex,end_vertex):
    distances = {vertex: float('infinity') for vertex in graph}
    distances[starting_vertex] = 0

    pq = [(0, starting_vertex)]
    while len(pq) > 0:
        current_distance, current_vertex = heapq.heappop(pq)

        # Nodes can get added to the priority queue multiple times. We only
        # process a vertex the first time we remove it from the priority queue.
        if current_distance > distances[current_vertex]:
            continue

        for neighbor, weight in graph[current_vertex].items():
            distance = current_distance + weight["length"]

            # Only consider this new path if it's better than any path we've
            # already found.
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                heapq.heappush(pq, (distance, neighbor))

    return distances[end_vertex]

def calculate_array(graph, starting_vertex,end_vertex):
    distances = {vertex: float('infinity') for vertex in graph}
    distances[starting_vertex] = 0
   
    visited = [False for _ in range(len(graph))]

    while True: 
        shortest_distance = float("inf")
        shortest_index = -1
        for i in range(len(graph)):  
            if distances[i] < shortest_distance and not visited[i]:
                shortest_distance = distances[i]
                shortest_index = i
        if shortest_index == -1:
            return distances[end_vertex]
        for neighbor, weight in graph[shortest_index].items():
            if distances[neighbor] > distances[shortest_index] + weight["length"]:
                distances[neighbor] = distances[shortest_index] + weight["length"]
        visited[shortest_index] = True

#generate a cycle
class CycleGraph():
    def __init__(self):
        self.graph = nx.cycle_graph(1000) #create a cycle with 1000 nodes
        nx.set_edge_attributes(self.graph, values = 1, name = 'length') #set weights to 1
        print(self.graph.edges(data=True))


    #function to add random edges
    def add_edges(self, num_edges,edge_length):
        nodes = range(1000)
        edges = list(self.graph.edges())
        edges += [(x[1],x[0]) for x in edges]
        
        possible_edges = [x for x in list(itertools.product(nodes,nodes)) if x[0]!=x[1] and x not in edges]
        sampled_edges = random.sample(possible_edges,num_edges)
        self.graph.add_edges_from(sampled_edges,length=edge_length)
        return

    def find_average_distances(self,graph_dict,z):
        nodes_list = list(self.graph.nodes())
        pairs = [random.sample(nodes_list, 2) for _ in range(z)]
        arr_val,arr_time = self.dijkstra_array(graph_dict,pairs)
        heap_val,heap_time = self.dijkstra_heap(graph_dict,pairs)
        assert heap_val==arr_val
        val = np.array(arr_val)
        return np.mean(val),np.std(val),arr_time,heap_time

    def dijkstra_array(self,graph_dict,pairs):
        distances = []
        start_time = timer()
        for start,end in pairs:
            dist = calculate_array(graph_dict,start,end)
            distances.append(dist)
        end_time = timer()
        total_time = end_time-start_time
        return distances, total_time

    def dijkstra_heap(self,graph_dict,pairs):
        distances = []
        start_time = timer()
        for start,end in pairs:    
            dist = calculate_heap(graph_dict,start,end)
            distances.append(dist)
        end_time = timer()
        total_time = end_time-start_time
        return distances, total_time
    
    def draw(self):
        plt.figure(1,figsize=(12,12))
        #pos=nx.drawing.nx_agraph.graphviz_layout(self.graph)
        #nx.draw_networkx(self.graph,pos)
        nx.draw_circular(self.graph, node_size=0.1)
        #nx.draw(self.graph, node_size=10)
        plt.savefig("graph_100.png")

class Experiment():
    def __init__(self,outfile):
        self.outfile = outfile


    def run(self,range_x,range_y,range_z):
        for x in range_x:
            for y in range_y:
                new_graph = CycleGraph()
                new_graph.add_edges(x,y)#add x edges of length 
                graph_dict = nx.to_dict_of_dicts(new_graph.graph)
                for z in range_z:
                    self.run_single_scenario(new_graph,graph_dict,x,y,z)

    def run_single_scenario(self,new_graph,graph_dict,x,y,z):
        av_dist,std,arr_time,heap_time = new_graph.find_average_distances(graph_dict,z)
        self.save(x,y,z,av_dist,std,arr_time,heap_time)
        return

    def save(self,x,y,z,d,std,arr_time,heap_time):
        df_new = pd.DataFrame([[x,y,z,d,std,arr_time,heap_time]])
        df_new.columns = ["x","y","z","d","std","arr_time","heap_time"]
        df_new.to_csv(self.outfile, mode='a', header=not os.path.exists(self.outfile),index=None)


class Statistics():
    def __init__(self,results_csv):
        with open(results_csv) as f:
            self.df = pd.read_csv(f)
        
    def generate_dataframes(self):
        return self.df.iloc[800:]
        #df_comparison = 

    def finding_z(self):
        df = self.df.iloc[800:]
        df1 = df[df["z"]==1]
        df10 = df[df["z"]==10]
        df100 = df[df["z"]==100]
        df1000 = df[df["z"]==1000]
        list1 = df1["z"].to_list()
        list10 = df10["z"].to_list()
        list100 = df100["z"].to_list()
        list1000 = df1000["z"].to_list()
        diff_1 = []
        diff_10 = []
        diff_100 = []
        for i in range(len(list1)):
            d1 = np.abs((list10[i]-list1[i]))
            d10 = np.abs((list100[i]-list10[i]))
            d100 = np.abs((list1000[i]-list100[i]))
            diff_1.append(d1)
            diff_10.append(d10)
            diff_100.append(d100)
        print(np.mean(diff_1),np.mean(diff_10),np.mean(diff_100))
        


    def compare_xy(self):
        pass
    def compare_time(self):
        pass


if __name__ == "__main__":
    #s = Statistics("results.csv")
    #a = s.generate_dataframes()
    #draw a graph 
    #g = CycleGraph()
    #g.add_edges(5,3)
    #print("after")
    #for e in g.graph.edges(data=True):
    #    print(e)
    g = CycleGraph()
    g.add_edges(100,3)
    g.draw()

    #max x: 499500
    #e = Experiment("results.csv")
    #range_x = list(range(1,499500,500))#max num edges to add: 499 500
    #range_y = list(range(1,1000))#there will be always a shortest minimal path d=500
    #range_z = [10**x for x in range(6)]
    #e.run(range_x,range_y,range_z)
    #e.run([1,10,1000],[1,2,3],[1,1000,10000])