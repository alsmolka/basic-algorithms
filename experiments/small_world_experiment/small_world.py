import networkx as nx
import random
import matplotlib.pyplot as plt

from timeit import default_timer as timer

#https://www.algorithms-and-technologies.com/dijkstra/python
https://bradfieldcs.com/algos/graphs/dijkstras-algorithm/
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

"""
def calculate_distances(graph, starting_vertex):
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
            distance = current_distance + weight

            # Only consider this new path if it's better than any path we've
            # already found.
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                heapq.heappush(pq, (distance, neighbor))

    return distances
"""
"""
def dijkstra(graph, start):
    """
    #Implementation of dijkstra using adjacency matrix.
    #This returns an array containing the length of the shortest path from the start node to each other node.
    #It is only guaranteed to return correct results if there are no negative edges in the graph. Positive cycles are fine.
    #This has a runtime of O(|V|^2) (|V| = number of Nodes), for a faster implementation see @see ../fast/Dijkstra.java (using adjacency lists)

    #:param graph: an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge or 0 if there is no edge.
    #:param start: the node to start from.
    #:return: an array containing the shortest distances from the given start node to each other node
    """
    # This contains the distances from the start node to all other nodes
    distances = [float("inf") for _ in range(len(graph))] #its an array! yay!

    # This contains whether a node was already visited
    visited = [False for _ in range(len(graph))]

    # The distance from the start node to itself is of course 0
    distances[start] = 0

    # While there are nodes left to visit...
    while True:

        # ... find the node with the currently shortest distance from the start node...
        shortest_distance = float("inf")
        shortest_index = -1
        for i in range(len(graph)):
            # ... by going through all nodes that haven't been visited yet
            if distances[i] < shortest_distance and not visited[i]:
                shortest_distance = distances[i]
                shortest_index = i

        # print("Visiting node " + str(shortest_index) + " with current distance " + str(shortest_distance))

        if shortest_index == -1:
            # There was no node not yet visited --> We are done
            return distances

        # ...then, for all neighboring nodes that haven't been visited yet....
        for i in range(len(graph[shortest_index])):
            # ...if the path over this edge is shorter...
            if graph[shortest_index][i] != 0 and distances[i] > distances[shortest_index] + graph[shortest_index][i]:
                # ...Save this path as new shortest path.
                distances[i] = distances[shortest_index] + graph[shortest_index][i]
                # print("Updating distance of node " + str(i) + " to " + str(distances[i]))

        # Lastly, note that we are finished with this node.
        visited[shortest_index] = True
        # print("Visited nodes: " + str(visited))
        # print("Currently lowest distances: " + str(distances))

"""
#generate a cycle
class CycleGraph():
    def __init__(self):
        self.graph = nx.cycle_graph(1000) #create a cycle with 1000 nodes
        nx.set_edge_attributes(self.graph, values = 1, name = 'length') #set weights to 1

    #function to add random edges
    def add_edges(self, num_edges,edge_length):
        for x in range(num_edges):
            #randomly select 2 nodes
            random_nodes = random.sample(list(self.graph.nodes()), 2)
            #generate a new edge
            self.graph.add_edge(random_nodes[0], random_nodes[1], length=edge_length)
        return

    #calculate shortest distance for a random pair using two different data structures
    def shortest_path(self):
        sp_array = [] #returns value, time to run
        sp_heap = [] #returns value, time to run
        assert sp_array[0]==sp_heap[0] #make sure both versions calculated ok
        return sp_array, sp_heap
    
    #calculate average shortest distance time 
    def average_shortest_path(self,z):
        for x in range(z):
            dist = self.shortest_path()

    def dijkstra_array():
        start = timer()
        sp = []
        end = timer()
        total_time = end-start

        return sp, total_time

    def dijkstra_heap():
        start = timer()
        sp = []
        end = timer()
        total_time = end-start
        return sp, total_time
    
    def draw(self):
        plt.figure(1,figsize=(12,12))
        #pos=nx.drawing.nx_agraph.graphviz_layout(self.graph)
        #nx.draw_networkx(self.graph,pos)
        #nx.draw_circular(self.graph, node_size=0.1)
        nx.draw(self.graph, node_size=10)
        plt.savefig("graph_100.png")

class Experiment():
    def __init__(self):
        pass


    def single_loop(self,num_edges,edge_length,z):
        pass

if __name__ == "__main__":
    g = CycleGraph()
    g.add_edges(100,3)
    g.draw()
