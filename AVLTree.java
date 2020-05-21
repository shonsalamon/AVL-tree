
/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */
//ShonSalamon - 313431967


public class AVLTree {
	private IAVLNode root;
	private IAVLNode min = null, max = null;


	//Constructor for empty tree
	public AVLTree() {
		
		this.root = new AVLNode(-1, null);
		this.min = this.root;
		this.max = this.root;
	}

	//Constructor for tree with given key and info 
	public AVLTree(int key, String val) {
		IAVLNode root = new AVLNode(key, val);
		this.root = root;
		this.min = this.root;
		this.max = this.root;
	}
	


  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
	  if(this.root.getHeight() == -1) {
		  return true;
	  }
    return false; 
  }
 
 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  IAVLNode node = searchRec(k, this.root);
	  if (node == null) {
		  return null;
	  }
	  return node.getValue();	
  }
  private IAVLNode searchRec(int k, IAVLNode node) {// search  item with key k using Recursion
	  int tmp = node.getKey();
	  if (k ==  tmp) {
		  return node;
	  } else if(tmp == -1) {
		  return null;
	  } else if (k < tmp ) {
		  return  searchRec(k, node.getLeft());
	  } else 
		  return searchRec(k, node.getRight());
	  	  
}

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
		//build new node with the given key and info
		IAVLNode newNode = new AVLNode(k, i);
		
		//check if the tree is empty
		if (empty()) {
			this.root = newNode;
			this.root.setParent(null);
			this.max = newNode;
			this.min = newNode;
			newNode.getLeft().setParent(newNode);
			newNode.getRight().setParent(newNode);
			return 0;
		}
		
		if (search(k) != null) 
			return -1;
		
		//update min and max of the tree
		if (k < this.min.getKey()){
			this.min = newNode;
		}
		if (k > this.max.getKey()) {
			this.max = newNode;
		}
		IAVLNode insertPlace = insertPlace(this.root, k);// finding the parent of the node we need to add
		newNode.setParent(insertPlace);
		newNode.getLeft().setParent(newNode);
		newNode.getRight().setParent(newNode);
		if (k > insertPlace.getKey()) {
			insertPlace.setRight(newNode);
			if (insertPlace.getLeft().getKey() != -1) {		// if there is a place to insert without need to balance
				updateHeight(newNode);
				updateSize(newNode);
				return 0;
			}			
		}
		else {
			insertPlace.setLeft(newNode);
			if (insertPlace.getRight().getKey() != -1) {	// if there is a place to insert without need to balance	
				updateHeight(newNode);
				updateSize(newNode);	
				return 0;
			}	
		}

		int cnt = checkBalance(insertPlace);// check the balance of the tree after insertion, and if need, balance
		updateHeight(newNode);
		updateSize(newNode);
		return cnt;		
   }
   /**
    * IAVLNode insertPlace(IAVLNode node, int k)
    *
    * returns the insertion point 
    */
   private IAVLNode insertPlace(IAVLNode node, int k) {
	   IAVLNode tmpNode = null;
	   while (node.getValue() != null) {
		    tmpNode = node;
		    if(k < node.getKey()) {
		   		node = node.getLeft();	
		   	} else 
		   		node = node.getRight();	
		   	}
		   	
	   return tmpNode;   
   }
   /**
    * updateHeight(IAVLNode node)
    *
    * update all nodes in the path we added a node. 
    */
	private void updateHeight(IAVLNode node) {
		
		node = node.getParent();
		while (node != null) {
			node.setHeight(Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);
			node = node.getParent();
		}
	}
	/**
	* updateSize(IAVLNode node)
	*
	* update all nodes in the path we added a node. 
	*/
	private void updateSize(IAVLNode node) {
		
		while (node.getParent() != null) {
			node = node.getParent();
			node.setSize(node.getLeft().getSize() + node.getRight().getSize() + 1);
		}
	}
	
	
	   /**
	    * promote(IAVLNode node)
	    *
	    * Increases height by one. 
	    */
   private void promote(IAVLNode node) {
	   node.setHeight(node.getHeight() +1 );
   }
   
   
   /**
    * demote(IAVLNode node)
    *
    * decrease height by one. 
    */
   private void demote(IAVLNode node) {
	   node.setHeight(node.getHeight() - 1 );
   }
   
   /**
    * int checkBalance(IAVLNode node)
    *
    * checks if tree is balanced after insertion. if not, it's Balances the tree 
    * and returns the number of balance operations. 
    */
  
   private int checkBalance(IAVLNode node) {
	   	int cnt = 0;
		while (node != null) {
			int leftRankDiffer = node.getHeight() - node.getLeft().getHeight();
			int rightRankDiffer = node.getHeight() - node.getRight().getHeight();

			if (( leftRankDiffer == 0 && rightRankDiffer == 1)|| (leftRankDiffer == 1 && rightRankDiffer == 0))	 {
				promote(node);
				cnt++;
			}
			if((leftRankDiffer == 2 && rightRankDiffer == 0)||(leftRankDiffer == 0 && rightRankDiffer == 2)) {
				cnt += balanceAfterInsert(node, leftRankDiffer, rightRankDiffer);
			}		
			node = node.getParent();	
		}
	   return cnt;
		}
   /**
    * int balanceAfterInsert(IAVLNode node, int leftRankDiffer, int rightRankDiffer )
    *
    * The tree is unbalanced, check which rotation need to do. 
    * rotates and than promote/demote as needed. returns the number of balance operations.
    *
    */
	private int balanceAfterInsert(IAVLNode node, int leftRankDiffer, int rightRankDiffer ) {
		int leftLeftRankDiffer = 0;
		int leftrightRankDiffer = 0;
		int rightRightRankDiffer = 0;
		int rightLeftRankDiffer = 0;
		
		if (node.getLeft().getKey() != -1){
			leftLeftRankDiffer = node.getLeft().getHeight()- node.getLeft().getLeft().getHeight();
			leftrightRankDiffer = node.getLeft().getHeight()- node.getLeft().getRight().getHeight();
		}
		if (node.getRight().getKey() != -1){
			rightRightRankDiffer = node.getRight().getHeight() - node.getRight().getRight().getHeight();
			rightLeftRankDiffer = node.getRight().getHeight() - node.getRight().getLeft().getHeight();
		}

		// LL Rotation - Rotate to right
		if (leftRankDiffer == 0 && rightRankDiffer == 2 && leftLeftRankDiffer == 1 && leftrightRankDiffer == 2) {
			LLrotation(node);
			demote(node);
			return 2;
		}
	
		// RR Rotation-Rotate to left
		if (rightRankDiffer == 0 && leftRankDiffer == 2 && rightRightRankDiffer == 1 && rightLeftRankDiffer == 2) {
			RRrotation(node);
			demote(node);
			return 2;
		}

		// LR Rotation - Rotate to left and than right
		if (leftRankDiffer == 0 && rightRankDiffer == 2 && leftLeftRankDiffer == 2 && leftrightRankDiffer == 1) {
			LRrotation(node);
			demote(node);
			demote(node.getParent().getLeft());
			promote(node.getParent());
			return 5;
		}
		
		// RL Rotation - Rotate to right and than left
		if (rightRankDiffer == 0 && leftRankDiffer == 2 && rightRightRankDiffer == 2 && rightLeftRankDiffer == 1) {
			RLrotation(node);
			demote(node);
			demote(node.getParent().getRight());
			promote(node.getParent());
			return 5;
		}
		
		
		// optional cases after join
		if (leftRankDiffer == 0 && rightRankDiffer == 2 && leftLeftRankDiffer == 1 && leftrightRankDiffer == 1) {
			IAVLNode tmp = node.getLeft();
			LLrotation(node);
			promote(tmp);
			return 2;
		}
		if (rightRankDiffer == 0 && leftRankDiffer == 2 && rightRightRankDiffer == 1 && rightLeftRankDiffer == 1) {
			IAVLNode tmp = node.getRight();
			RRrotation(node);
			promote(tmp);
			return 2;
		}
		
		return 0;	
	}

		
	
	/**
	 * LLrotation(IAVLNode node)
	 * 
	 * rotate the tree to right
	 */
	private void LLrotation(IAVLNode node) {//rotate right
	
		IAVLNode tmpLR = node.getLeft().getRight();
		IAVLNode tmpLeft = node.getLeft();
		IAVLNode prevNode  = node;
		
		if (prevNode.getParent() != null) {
			if(prevNode.getParent().getKey() > prevNode.getKey()) {
				prevNode.getParent().setLeft(tmpLeft);
			}
			else {
				prevNode.getParent().setRight(tmpLeft);
			}
		}
		
		tmpLeft.setParent(prevNode.getParent());
		tmpLeft.setRight(prevNode);
		prevNode.setParent(tmpLeft);
		tmpLR.setParent(prevNode);
		prevNode.setLeft(tmpLR);

		// update size
		prevNode.setSize(tmpLR.getSize() + prevNode.getRight().getSize() + 1);
		tmpLeft.setSize(tmpLeft.getLeft().getSize() + prevNode.getSize() + 1);
		updateSize(tmpLeft);
		
	
		if (tmpLeft.getParent() == null){
			this.root = tmpLeft;
		}
	
	/**
	 * RRrotation(IAVLNode node)
	 * 
	 * rotate the tree to left
	 */
	}
	private void RRrotation(IAVLNode node) {
		IAVLNode tmpRL = node.getRight().getLeft();
		IAVLNode tmpRight = node.getRight();
		IAVLNode prevNode  = node;
		tmpRight.setParent(prevNode.getParent());
		if (prevNode.getParent() != null) {
			if(prevNode.getParent().getKey() > prevNode.getKey()) {
				prevNode.getParent().setLeft(tmpRight);
			}
			else {
				prevNode.getParent().setRight(tmpRight);
			}
		}
		prevNode.setParent(tmpRight);
		tmpRL.setParent(prevNode);
		tmpRight.setLeft(prevNode);
		prevNode.setRight(tmpRL);
		
		// update size
		prevNode.setSize(prevNode.getLeft().getSize() + tmpRL.getSize() + 1);
		tmpRight.setSize(prevNode.getSize() + tmpRight.getRight().getSize() + 1);
		updateSize(tmpRight);
				
		
		if (tmpRight.getParent() == null) {
			this.root = tmpRight;
		}
		
	}	
	/**
	 * LRrotation(IAVLNode node)
	 * 
	 * rotate to left and than to right
	 */
	private void LRrotation(IAVLNode node) {  
		RRrotation(node.getLeft());
		LLrotation(node);
		
	}
	/**
	 * RLrotation(IAVLNode node)
	 * 
	 * rotate to right and than to left
	 */
	private void RLrotation(IAVLNode node) { 
		LLrotation(node.getRight());
		RRrotation(node);
		
	}
	

	
	  /**
	   * public int delete(int k)
	   *
	   * deletes an item with key k from the binary tree, if it is there;
	   * the tree must remain valid (keep its invariants).
	   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	   * returns -1 if an item with key k was not found in the tree.
	   */
		
	public int delete(int k){
		//k not in the tree or empty tree
		if (this.empty() || search(k) == null) 
			return -1; 
		// tree with one node only (and we already checked that its k)
		if (this.root.getSize()==1) { 
			this.root = new AVLNode(-1,null);
			this.root.setParent(null);
			this.min = null;
			this.max = null ;
			return 0;
		 } 
		int cnt = 0; 
		IAVLNode cur_node = BSTdelete(k); // regular deletion that return the parent 
		IAVLNode tmpCur_node = cur_node;
		
		// go up to the root
		while (cur_node != null) {
			int leftRankDiffer = cur_node.getHeight() - cur_node.getLeft().getHeight();
			int rightRankDiffer = cur_node.getHeight() - cur_node.getRight().getHeight();
			  
			//case 1: Demote
			if(leftRankDiffer == 2 && rightRankDiffer == 2 ) {
				demote(cur_node); 
			}
			int leftLeftRankDiffer = 0;
			int leftrightRankDiffer = 0;
			int rightRightRankDiffer = 0;
			int rightLeftRankDiffer = 0;
			if (cur_node.getLeft().getKey() != -1){
				leftLeftRankDiffer = cur_node.getLeft().getHeight()- cur_node.getLeft().getLeft().getHeight();
				leftrightRankDiffer = cur_node.getLeft().getHeight()- cur_node.getLeft().getRight().getHeight();
			}
			if (cur_node.getRight().getKey() != -1){
				rightRightRankDiffer = cur_node.getRight().getHeight() - cur_node.getRight().getRight().getHeight();
				rightLeftRankDiffer = cur_node.getRight().getHeight() - cur_node.getRight().getLeft().getHeight();
			}
			  
			//case 2: single rotation to left
			if(leftRankDiffer == 3 && rightRankDiffer == 1 && rightRightRankDiffer == 1 && rightLeftRankDiffer == 1) {
				IAVLNode tmp = cur_node.getRight(); 
				 RRrotation(cur_node);
				 demote(cur_node);
				 promote(tmp);
				 cnt += 3;
				  
			}
			//case 2: single rotation to right
			if(rightRankDiffer == 3 && leftRankDiffer == 1 && leftLeftRankDiffer == 1 && leftrightRankDiffer == 1) {
				IAVLNode tmp = cur_node.getLeft();
				LLrotation(cur_node);
				demote(cur_node);
				promote(tmp);
				cnt += 3;
				  
			}
			
			//case 3: single rotation to left
			if(leftRankDiffer == 3 && rightRankDiffer == 1 && rightRightRankDiffer == 1 && rightLeftRankDiffer == 2 ) {
				RRrotation(cur_node);
				demote(cur_node);
				demote(cur_node);
				cnt += 3;
			}
			//case 3: single rotation to right
			if(rightRankDiffer == 3 && leftRankDiffer == 1 && leftLeftRankDiffer == 1 && leftrightRankDiffer == 2 ) {
				LLrotation(cur_node);
				demote(cur_node);
				demote(cur_node);
				cnt += 3;  
			}
			   
			//case 4: double rotation -  rotate to right and than to left
			if(leftRankDiffer == 3 && rightRankDiffer == 1 && rightRightRankDiffer == 2 && rightLeftRankDiffer == 1 ) {
				IAVLNode tmp = cur_node.getRight();
				IAVLNode tmp1 = cur_node.getRight().getLeft();
				RLrotation(cur_node);
				demote(cur_node);
				demote(cur_node);
				demote(tmp);
				promote(tmp1);
				cnt += 6;	  
			}
			//case 4: double rotation -  rotate to left and than to right
			if(rightRankDiffer == 3 && leftRankDiffer == 1 && leftLeftRankDiffer == 2 && leftrightRankDiffer == 1 ) {
				IAVLNode tmp = cur_node.getLeft();
				IAVLNode tmp1 = cur_node.getLeft().getRight();
				LRrotation(cur_node);
				demote(cur_node);
				demote(cur_node);
				demote(tmp);
				promote(tmp1);
				cnt += 6;  
			} 
			cur_node = cur_node.getParent(); 
		 }
		if(tmpCur_node != null) {
			updateHeight(tmpCur_node);
			updateSize(tmpCur_node);
		}
		return cnt;	   
	}
	   
	  /**
	   * IAVLNode BSTdelete(int k)
	   *
	   * deletes an item with key k from the binary tree.
	   * 
	   */
	   public IAVLNode BSTdelete(int k) { //O(logn) - in that case k in the tree
		   
		   IAVLNode node = this.root;
		   IAVLNode parent = node.getParent();
		   boolean flagMax = false;
		   boolean flagMin = false;
		   if (this.max.getKey() == k) 
			   flagMax = true ;
		   if (this.min.getKey() == k) 
			   flagMin = true;
			
			//search the node we need to delete
			while(node.getKey() != k) {
				if(k > node.getKey()) {
					parent = node;
					node = node.getRight();
					}
				else {
					parent = node;
					node = node.getLeft();
				}			
			}
				
			//case 1: node is leaf
			if (node.getHeight() == 0) {
				deleteLeaf(node);
			}
			//case 2: unary
			else if(!node.getLeft().isRealNode() || !node.getRight().isRealNode()) {
				passBy(node);
			}
			
			// case 3: there is two children for this node
			else if (node.getLeft().isRealNode() && node.getRight().isRealNode()) { 
				boolean flagRoot = false;
				if(node == this.root) {
					flagRoot = true;
				}
				IAVLNode sucNode = successor(node);
				if(sucNode.getHeight() == 0) {	
					IAVLNode tmpParent = sucNode.getParent();
					replace(node,sucNode);
					deleteLeaf(sucNode);
					tmpParent.setHeight(Math.max(tmpParent.getLeft().getHeight(), tmpParent.getRight().getHeight()) + 1);
					if(flagRoot) {
						parent = tmpParent;
					}else {
						parent = node;
					}
				} 
				else if(!sucNode.getLeft().isRealNode() && sucNode.getRight().isRealNode()) { //the succ can be only right child, otherwise the left chils is the succ
					parent = sucNode;
					replace(node,sucNode);
					passBy(sucNode);
				}
			}
			if(flagMax)//checks if the node we deleted was the maximum.	
				this.max = findMax(this.root);
			if(flagMin)//checks if the node we deleted was the minimum.	
				this.min = findMin(this.root);
			return parent;

	   }   
		  /**
		   * deleteLeaf(IAVLNode leaf)
		   *
		   * deletes an leaf from the tree.
		   * 
		   */
	   private void deleteLeaf(IAVLNode leaf) {
		   IAVLNode parent = leaf.getParent();
			if (parent.getLeft() == leaf) {
				parent.setLeft(new AVLNode(-1,null));
				parent.getLeft().setParent(parent);
			}	
			else if (parent.getRight() == leaf) { 
				parent.setRight(new AVLNode(-1,null));
				parent.getRight().setParent(parent);
			}
			updateSizeAfterDeleteForSuccessor( leaf);
			leaf.setParent(null);
	   }
	   
		  /**
		   * replace(IAVLNode deleteNode,IAVLNode successor)
		   *
		   * replace between node and his successor.
		   * 
		   */
	   private void replace(IAVLNode deleteNode,IAVLNode successor) { 
		   int key = deleteNode.getKey();
		   String val = deleteNode.getValue();
		   deleteNode.setKey(successor.getKey());
		   deleteNode.setValue(successor.getValue());
		   successor.setKey(key);
		   successor.setValue(val);
		   
		  
	   }
		  /**
		   * passBy(IAVLNode NodeToPass)
		   *
		   * skip the node to his son.
		   * 
		   */
	   private void passBy(IAVLNode NodeToPass) {
		   if(NodeToPass.getParent() == null) {
			   if(NodeToPass.getLeft().getKey() != -1) {	// the son is on the left of the node
				   NodeToPass.getLeft().setParent(null);
				   this.root = NodeToPass.getLeft();
			   }else {										// the son is on the right of the node
				   NodeToPass.getRight().setParent(null);
				   this.root = NodeToPass.getRight();
			   }
		   }else {
			   IAVLNode parent = NodeToPass.getParent();
		   		if(parent.getLeft() == NodeToPass) {				// we came from right
		   		if(NodeToPass.getLeft().getKey() != -1) {	// the son is on the left of the node
		   			parent.setLeft(NodeToPass.getLeft());
		   			NodeToPass.getLeft().setParent(parent);
		   		}else {										// the son is on the right of the node
		   			parent.setLeft(NodeToPass.getRight());
		   			NodeToPass.getRight().setParent(parent);   
		   		}
		   		}else {											// we came from left
		   			if(NodeToPass.getLeft().getKey() != -1) {	// the son is on the left of the node
		   				parent.setRight(NodeToPass.getLeft());
		   				NodeToPass.getLeft().setParent(parent);
		   			}else 										{// the son is on the right of the node
		   				parent.setRight(NodeToPass.getRight());
		   				NodeToPass.getRight().setParent(parent);
		   			}
		   		} 
		   }
	   }
	   
		  /**
		   * updateSizeAfterDeleteForSuccessor(IAVLNode node)
		   *
		   * updates all size that were influenced from the deletion. 
		   * 
		   */  
	private void updateSizeAfterDeleteForSuccessor(IAVLNode node) {
			while (node != null) {
				node.setSize((node.getLeft().getSize() + node.getRight().getSize()) + 1);
				node = node.getParent();
			}
		}
	
	  /**
	   * IAVLNode successor(IAVLNode node)
	   *
	   * find the successor of the node 
	   * 
	   */ 
	public IAVLNode successor(IAVLNode node) { //O(logn) -  find the successor of the node
		   if(!node.isRealNode()) 
			   return null; 
		  
		   if(node.getRight().isRealNode()) {
			   node = node.getRight();
			   while(node.getLeft().isRealNode())
				   node = node.getLeft();
			   return node;
		   }
			  
		   else {
			   IAVLNode tmp = node;
			   IAVLNode x = tmp.getParent();
			   while(x.isRealNode() && x.getLeft()!=tmp) {
				   tmp = x;
				   x = x.getParent();
			   }
			   return x;
		   }
	   }
	
	  /**
	   * IAVLNode predecessor(IAVLNode node)
	   *
	   * find the predecessor of the node 
	   * 
	   */
	   public IAVLNode predecessor(IAVLNode node) { // O(logn) - find the predecessor of the node
		
		   if(!node.isRealNode()) 
			   return null; 
		
		   if(node.getLeft().isRealNode()) {
			   node = node.getLeft(); 
			   while(node.getRight().isRealNode()) 
				   node =  node.getRight();   
			   return node;
		   }
		   else {
			   IAVLNode tmp = node;
			   IAVLNode x = tmp.getParent();
			   while(x.isRealNode() && x.getRight()!=tmp) {
				   tmp = x;
				   x = x.getParent();
			   }
			   return x;
		   }
	   }
   
   
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   return this.min.getValue(); 
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   return this.max.getValue(); 
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   
  
  private static int[] keys_arr = null;
  private static int index_of_keys_arr = 0;
  private static String[] info_arr = null;
  private static int index_of_info_arr = 0;
    
   
  public int[] keysToArray() {
	  if(empty()) 
		  return (new int [0]);  
	  keys_arr = new int[size()];
	  index_of_keys_arr=0;
	  orderKeysToArray(this.root);
      return keys_arr;
  }
  
  	private void orderKeysToArray(IAVLNode node) {
  		if(node.getKey()!=-1) {
  			orderKeysToArray(node.getLeft());
  			keys_arr[index_of_keys_arr] = node.getKey();
  			index_of_keys_arr++;
			orderKeysToArray(node.getRight());
  		}
	  
  	}

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  if(empty())
		  return (new String [0]);
	  info_arr = new String [size()];
	  index_of_info_arr = 0;
	  orderInfoToArray(this.root);
	     return info_arr;            
  }
	 
	 
  private void orderInfoToArray(IAVLNode node) {  
	  if(node.getKey()!=-1) {
		orderInfoToArray(node.getLeft());
		info_arr[index_of_info_arr] = node.getValue();
		index_of_info_arr++;
		orderInfoToArray(node.getRight());
	  }
  }
	                         
   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return this.root.getSize();
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   if (empty())
		   return null; 
	   return this.root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null
    * postcondition: none
    */   
   public AVLTree[] split(int x)
   {
	   AVLTree t1 = new AVLTree();
	   AVLTree t2 = new AVLTree();
	   if(this.min.getKey() == x) {// checks if need to split from minimum, if yes, delete him.
		   int cnt = this.delete(x);
		   t2 = this;   
		   AVLTree [] array = new AVLTree [2];
		   array[0] = t1;
		   array[1] = t2;
		   return array;   
	   }
	   if(this.max.getKey() == x) {// checks if need to split from maximum, if yes, delete him.
		   int cnt = this.delete(x);
		   t1 = this;
		   AVLTree [] array = new AVLTree [2];
		   array[0] = t1;
		   array[1] = t2;
		   return array; 
	   }
	   
	   IAVLNode splitPlace = find(x);// finds the split place node.
	   t1.root = splitPlace.getLeft();
	   t2.root = splitPlace.getRight();
	   IAVLNode min1 = this.min;
	   IAVLNode max2 = this.max;
	   
	   IAVLNode tmpSplitPlace = splitPlace;
	   int j = 0;
	   while(tmpSplitPlace != null) {// finds how far is the root from the split place node.
		   j++;
		   tmpSplitPlace = tmpSplitPlace.getParent();
	   }
	   IAVLNode [] nodes = new IAVLNode [j];
	   IAVLNode tmpSplitPlace1 = splitPlace;
	   
	   for(int i = 0; i < nodes.length; i++) {// an array that will contain all the nodes we need to go through until the root.
		   nodes[i] = tmpSplitPlace1;
		   tmpSplitPlace1 = tmpSplitPlace1.getParent();
	   }  
	   for(int i = 0; i < nodes.length-1; i++) {
		   AVLTree tmpAdded = new AVLTree();
		   IAVLNode tmpRoot = nodes[i];
		   IAVLNode tmpParent = nodes[i + 1];
		   
		   if(tmpParent.getRight().getKey() == tmpRoot.getKey()) {// we arrived tmpRoot from left
			   tmpAdded.root = tmpParent.getLeft();
			   tmpAdded.root.setParent(null);
			   int cnt = t1.join(tmpParent,tmpAdded);
			   
		   
		   }else {// we arrived tmpRoot from right
			   tmpAdded.root = tmpParent.getRight();
			   tmpAdded.root.setParent(null);
			   int cnt = t2.join(tmpParent,tmpAdded);
		   }
	   }
		
	    t1.min = min1;
	    t1.max = findMax(t1.getRoot());
	    t2.min = findMin(t2.getRoot());
	    t2.max = max2;
	   AVLTree [] array = new AVLTree [2];
	   array[0] = t1;
	   array[1] = t2;
	   return array;
	   
   }
  /**
  * IAVLNode findMax(IAVLNode node)
  *
  * returns the maximum in the tree that node is his root. 
  * 
  * 
  */  
   private IAVLNode findMax(IAVLNode node) {
	   while(node.getRight().getKey() != -1) {
		   node = node.getRight();
	   }
	   return node;
   }
  /**
  * IAVLNode findMin(IAVLNode node)
  *
  * returns the minimum in the tree that node is his root. 
  * 
  * 
  */ 
   private IAVLNode findMin(IAVLNode node) {
	   while(node.getLeft().getKey() != -1) {
		   node = node.getLeft();
	   }
	   return node;
   }
  
   /**
  * IAVLNode find(int x)
  *
  * returns the node with key x. 
  * 
  * 
  */ 
   
   private IAVLNode find(int x) {
	   
		   IAVLNode tmpPlace = findRec(x, this.root);
		   if (tmpPlace == null) { 
			   return null;
			  }
			  return tmpPlace;	
	 }
   private IAVLNode findRec(int x, IAVLNode node) {//returns the node with key x using Recursion.
			  int tmp = node.getKey();
			  if (x ==  tmp) {
				  return node;
			  } else if(tmp == -1) {
				  return null;
			  } else if (x < tmp ) {
				  return  findRec(x, node.getLeft());
			  } else 
				  return findRec(x, node.getRight());
			  	  
		}
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (rank difference between the tree and t)
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys()
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t)
   {
	   if(t.empty()) {//checks if t is empty. if yes, add x to this tree.
		   int cnt = this.insert(x.getKey(), x.getValue());
		   return ((this.root.getHeight() - t.root.getHeight()) + 1 );  
	   }
	   if(this.empty()) {//checks if this is empty. if yes, add x to t tree, and than this to the result.
		   int tmpHeight = t.root.getHeight();
		   if(x.getKey() > t.max.getKey()) 
			   this.max = x;
		   else
			   this.max = t.max;
		   if(x.getKey() < t.min.getKey())
			   this.min = x;
		   else 
			   this.min = t.min;
		   int cnt = t.insert(x.getKey(), x.getValue());
		   this.root = t.root;

		   return (tmpHeight  + 2 );
	   }
	   AVLTree biggerTree;
	   AVLTree smallerTree;
	   if(t.getRoot().getHeight() ==  this.getRoot().getHeight()) {// if both trees as the same height
		   if(t.getRoot().getKey() > this.getRoot().getKey()) {
			   x.setLeft(this.getRoot());
			   this.getRoot().setParent(x);
			   x.setRight(t.getRoot());
			   t.getRoot().setParent(x);
			   x.setParent(null);
			   x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
			   x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
			   this.root = x;
			   this.max = t.max;
			   
		   }else {
			   x.setLeft(t.getRoot());
			   t.getRoot().setParent(x);
			   x.setRight(this.getRoot());
			   this.getRoot().setParent(x);
			   x.setParent(null);
			   x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
			   x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);  
			   this.root = x;
			   this.min = t.min;   
		   } 
		   
		   return 1;
		   
	   }
	   int biggerHeight = 0;
	   int smallerHeight = 0;
	   if(t.getRoot().getHeight() > this.getRoot().getHeight()) {//checks who is the "taller" tree.
		   biggerTree = t;
		   smallerTree = this;
		   biggerHeight = t.getRoot().getHeight();
		   smallerHeight = this.getRoot().getHeight();
	   }
	   else {
		   biggerTree = this;
		   smallerTree = t;
		   biggerHeight = this.getRoot().getHeight();
		   smallerHeight = t.getRoot().getHeight();
	   }
	   if(biggerTree.getRoot().getKey() > smallerTree.getRoot().getKey() ){ // check on which side to add the smaller tree
		   this.root = addOnLeft(biggerTree,x,smallerTree);
		   this.max = biggerTree.max;
		   this.min = smallerTree.min;
	   }
	   else {
		   this.root = addOnRight(biggerTree,x,smallerTree);
		   this.max = smallerTree.max;
		   this.min = biggerTree.min;
		   
	   		}
		  
	   return (biggerHeight - smallerHeight + 1 );
   }
   /**
    * IAVLNode addOnLeft(AVLTree biggerTree,IAVLNode x, AVLTree smallerTree)
    *
    * add the smaller tree on the left side of the bigger tree. 	
    * 
    * 
    */   
   private IAVLNode addOnLeft(AVLTree biggerTree,IAVLNode x, AVLTree smallerTree) { // Adding the small tree on the left size of the big tree
	   IAVLNode smaller = smallerTree.getRoot();
	   int rankOfSmaller = smaller.getHeight();
	   IAVLNode nodeLeftTmp = biggerTree.getRoot().getLeft();
	   while(nodeLeftTmp.getHeight() > rankOfSmaller ) { // search for the joining point
		   nodeLeftTmp = nodeLeftTmp.getLeft();
	   }
	   IAVLNode tmpParent = nodeLeftTmp.getParent();
	   x.setLeft(smaller);
	   smaller.setParent(x);
	   x.setRight(nodeLeftTmp);
	   nodeLeftTmp.setParent(x);
	   tmpParent.setLeft(x);	   
	   x.setParent(tmpParent);
	   x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
	   this.root = biggerTree.root;
	   
	   int cnt = checkBalance(x.getParent());// checking balance after the join
		
	   // update height
		updateHeight(x.getLeft());
		
		// update size
		updateSize(x.getLeft());   
 
	   return biggerTree.getRoot();
   
   }
   /**
    * IAVLNode addOnRight(AVLTree biggerTree,IAVLNode x, AVLTree smallerTree)
    *
    * add the smaller tree on the right side of the bigger tree. 	
    * 
    * 
    */ 
   private IAVLNode addOnRight(AVLTree biggerTree,IAVLNode x, AVLTree smallerTree) {// Adding the small tree on the right size of the big tree
	   IAVLNode smaller = smallerTree.getRoot();
	   int rankOfSmaller = smaller.getHeight();
	   IAVLNode nodeRightTmp = biggerTree.getRoot().getRight();
	   while(nodeRightTmp.getHeight() > rankOfSmaller ) {// search for the joining point
		   nodeRightTmp = nodeRightTmp.getRight();
	   }
	   IAVLNode tmpParent = nodeRightTmp.getParent();
	   x.setRight(smaller);
	   smaller.setParent(x);
	   x.setLeft(nodeRightTmp);
	   nodeRightTmp.setParent(x);
	   tmpParent.setRight(x);
	   x.setParent(tmpParent);
	   x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
	   this.root = biggerTree.root;
	  
	   int cnt = checkBalance(x.getParent()); // checking balance after the join
	  
	   // update height
		updateHeight(x.getRight());
		
		// update size
		updateSize(x.getRight());  
	   
	   return biggerTree.getRoot();
	   
	   
   }
	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtual node return -1)
		public void setKey(int k);//sets key
    	public void setValue(String v);//sets value
		public String getValue(); //returns node's value [info] (for virtual node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	public void setSize(int size); // sets the number of real nodes in this node's subtree
    	public int getSize(); // Returns the number of real nodes in this node's subtree
    	
	}
	

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  private int key, height,size;
	  private String info;
	  private IAVLNode left,right, parent; 
	 
	  public AVLNode(int key,String info,IAVLNode parent) {
		  this.key=key;
		  this.info=info;
		  this.height=0;
		  this.size=1;
		  this.parent=parent;
	  }
	 
	  
		/**
		 * AVLNode constructor - create a node with key and info
		 * @param key
		 * @param info
		 */
	  public AVLNode (int key,String info) {// AVLNode constructor 
		  if(key != -1) {
			  this.key = key;
			  this.info = info;
			  this.right = new AVLNode(-1,null);
			  this.left = new AVLNode(-1,null);
			  this.height = 0;
			  this.size = 1;
		  }
		  else {
			  this.key = key;
			  this.info = null;
			  this.height = -1;
			  this.size = 0;
		  }
		  
	  }
		public int getKey()
		{
			return this.key; 
		}
		public void setKey(int k)
		{
			this.key = k;
		}
		public String getValue()
		{
			return this.info; 
		}
		public void setValue(String v)
		{
			this.info = v;
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		public IAVLNode getLeft()
		{
			return this.left;
		}
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}
		public IAVLNode getRight()
		{
			return this.right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node; 
		}
		public IAVLNode getParent()
		{
			return this.parent; 
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			if(this == null || key == -1) {
				return false;
			}
			return true; 
		}
		public void setHeight(int height)
		{
			this.height = height;
		}
		public int getHeight()
		{
			return this.height; 
		}
		public void setSize(int size)
		{
			this.size = size;
		}
		
		public int getSize()
		{
			return this.size;
		}

  }
  
}

  

