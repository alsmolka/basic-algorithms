public import java.util.HashMap;

public class Example {
	public static void main(String[] args) {
		run_arrayofarrays();
		run_btree();
		run_hashtable();
		run_skiplist();
		run_treap();
	}
	
	public static void run_arrayofarrays() {
		//example for array of arrays
		//initialize a multidimentional array
		int [][] arrOfarr_empty = new int[1][];
		int k = 10; //specify number of elements to add
		//add elements
		int [][] arrOfarr = TimingArrayofArrays.increment(arrOfarr_empty, k);
		//run search (automatically generated 100K integers and searches for them)
		TimingArrayofArrays.run_search(arrOfarr);
		//print array
		//TimingArrayofArrays.print_array(arrOfarr);//prints the array 
	}
	public static void run_btree() {
		//example for BTree
		//initialize a btree
		BTree<Integer, Integer> btree_empty =new BTree<Integer, Integer>();
		int k = 10; //specify number of elements to add
		//add elements
		BTree<Integer, Integer> btree= TimingBTree.increment(btree_empty, k);
		//run search (automatically generated 100K integers and searches for them)
		TimingBTree.run_search(btree);
	}
	public static void run_hashtable() {
		//example for hash table
		//initialize an empty hash table (containing integers)
		HashMap<Integer, Integer> hash_table_empty =new HashMap<Integer, Integer>();
		int k = 10; //specify number of elements to add
		//add elements
		HashMap<Integer, Integer> hash_table = TimingHashTable.increment(hash_table_empty,k);
		//run search (automatically generated 100K integers and searches for them)
		TimingHashTable.run_search(hash_table);
	}
	public static void run_skiplist() {
		//example for skiplist
		//initialize an empty skiplist
		SkipList<Integer> skiplist_empty = new SkipList<Integer>();
		int k = 10; //specify number of elements to add
		//add elements
		SkipList<Integer> skiplist = TimingSkipList.increment(skiplist_empty,k);
		//run search (automatically generated 100K integers and searches for them)
		TimingSkipList.run_search(skiplist);
	}
	public static void run_treap() {
		//example for treap
		//initialize an empty treap
		Treap<Integer> treap_empty = new Treap<Integer>();
		int k = 10; //specify number of elements to add
		//add elements
		Treap<Integer> treap = TimingTreap.increment(treap_empty,k);
		//run search (automatically generated 100K integers and searches for them)
		TimingTreap.run_search(treap);
	}
}
 Example {
    
}
