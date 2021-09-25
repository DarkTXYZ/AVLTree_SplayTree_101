public class AVLTree extends BTreePrinter {
    Node root;

    public AVLTree(Node root) {
        this.root = root;
        root.parent = null;
    }

    public void singleRotateFromLeft(Node y) {
        /*           
         *           w                  w
         *          /                  /
         *         y                  x  
         *        / \       -->      / \
         *       x   \              /   y
         *      / \   \            /   / \
         *     a   b   c          a   b   c
         *  
         * หลักการ
         *  - เปลี่ยน right child ของ x เป็น y
         *  - เปลี่ยน left child ของ y เป็น b
         *  - เปลี่ยน child ของ w เป็น x (left or right ?)
         */
        Node x = y.left;
        Node w = y.parent;
        Node b = x.right;
        
        y.left = b;
        if (b != null)
            b.parent = y;
        y.parent = x;
        x.right = y;
        // y มี parent
        if (w != null) {
            x.parent = w;
            if (w.key < x.key)
                w.right = x;
            else
                w.left = x;
        }
        // y ไม่มี parent 
        else {
            root = x;
            root.parent = null;
        }
    }

    public void singleRotateFromRight(Node y) {
        /*           
         *           w                  w
         *          /                  /
         *         y                  x  
         *        / \       -->      / \
         *       /   x              y   \
         *      /   / \            / \   \
         *     a   b   c          a   b   c
         *  
         * หลักการ
         *  - เปลี่ยน left child ของ x เป็น y
         *  - เปลี่ยน right child ของ y เป็น b
         *  - เปลี่ยน child ของ w เป็น x (left or right ?)
         */
        
        Node x = y.right;
        Node w = y.parent;
        Node b = x.left;

        y.right = b;
        if (b != null)
            b.parent = y;
        y.parent = x;
        x.left = y;
        // y มี parent
        if (w != null) {
            x.parent = w;
            if (w.key < x.key)
                w.right = x;
            else
                w.left = x;
        } 
        // y ไม่มี parent
        else {
            root = x;
            root.parent = null;
        }
    }

    public void doubleRotateFromLeft(Node y) {
        /*           
         *           w                  w
         *          /                  /
         *         y                  z  
         *        / \       -->      / \
         *       x   \              /   \
         *      / \   \            /     \
         *     /   z   \          x       y
         *    /   / \   \        / \     / \   
         *   a   b   c   d      a   b   c   d
         * หลักการ
         *  - เปลี่ยน right child ของ x เป็น b
         *  - เปลี่ยน left child ของ y เป็น c
         *  - เปลี่ยน left , right child ของ z เป็น x และ y ตามลำดับ
         *  - เปลี่ยน child ของ w เป็น x (left or right ?)
         *  - Alt :
         *      doubleRotateFromLeft(Node y){
         *          Node x = y.left;
         *          singleRotateFromRight(x);
         *          singleRotateFromLeft(y);
         *      }
         */

        Node x = y.left;
        Node z = x.right;
        Node w = y.parent;

        Node b = z.left;
        Node c = z.right;

        x.right = b;
        if (b != null)
            b.parent = x;

        y.left = c;
        if (c != null)
            c.parent = y;

        z.left = x;
        z.right = y;
        x.parent = z;
        y.parent = z;
        // y มี parent
        if (w != null) {
            z.parent = w;
            if (z.key < w.key)
                w.left = z;
            else
                w.right = z;
        } 
        // y ไม่มี parent
        else {
            root = z;
            root.parent = null;
        }
    }

    public void doubleRotateFromRight(Node y) {
        /*           
         *           w                  w
         *          /                  /
         *         y                  z  
         *        / \       -->      / \
         *       /   x              /   \
         *      /   / \            /     \
         *     /   z   \          y       x
         *    /   / \   \        / \     / \   
         *   a   b   c   d      a   b   c   d
         * หลักการ
         *  - เปลี่ยน right child ของ y เป็น b
         *  - เปลี่ยน left child ของ x เป็น c
         *  - เปลี่ยน left , right child ของ z เป็น y และ x ตามลำดับ
         *  - เปลี่ยน child ของ w เป็น x (left or right ?)
         *  - Alt :
         *      doubleRotateFromLeft(Node y){
         *          Node x = y.right;
         *          singleRotateFromLeft(x);
         *          singleRotateFromRight(y);
         *      }
         */
        Node x = y.right;
        Node z = x.left;
        Node w = y.parent;

        Node b = z.left;
        Node c = z.right;

        y.right = b;
        if (b != null)
            b.parent = y;

        x.left = c;
        if (c != null)
            c.parent = x;

        z.left = y;
        z.right = x;
        x.parent = z;
        y.parent = z;

        // y มี parent
        if (w != null) {
            z.parent = w;
            if (z.key < w.key)
                w.left = z;
            else
                w.right = z;
        } 
        // y ไม่มี parent
        else {
            root = z;
            root.parent = null;
        }
    }

    public static void rebalance(AVLTree tree, Node node) {
        /*
         *           X                   
         *          / \   
         *         /   \      
         *        L     R             
         *       / \   / \           
         *      OL IL IR OR           
         * หลักการ
         *  - check ว่า grandchild ของ node X ตัวไหน ทำให้ tree unbalance
         * วิธี  
         * > check node X ก่อนว่า unbalanced มั้ย
         * > ถ้า unbalanced , check ว่า tree หนักไปทางไหน (left-heavy or right-heavy) 
         * > จากนั้น ให้เข้าไปยัง sub-tree ทางนั้น แล้ว check อีกว่า sub-tree นั้น หนักไปทางไหน (left-heavy or right-heavy) 
         * > เราจะรู้แล้วว่า grandchild ตัวไหนทำให้ unbalanced (OL(Outer Left), IL(Inner Left) , IR(Inner Right) , OR(Outer Right))
         *      > ถ้าเป็น OL --> singleRotateFromLeft(X)
         *      > ถ้าเป็น IL --> doubleRotateFromLeft(X)
         *      > ถ้าเป็น IR --> doubleRotateFromRight(X)
         *      > ถ้าเป็น OR --> singleRotateFromRight(X)
         */
        if (node == null)
            return;
        int balanceFactor = height(node.left) - height(node.right); // คำนวณ balanceFactor check ว่า unbalanced มั้ย
        
        if (Math.abs(balanceFactor) > 1) { // ถ้า unbalanced
            if (balanceFactor > 0) { // left-heavy
                int balanceFactor_next = height(node.left.left) - height(node.left.right);
                if (balanceFactor_next > 0) { //subtree left-heavy (OL)
                    tree.singleRotateFromLeft(node); 
                    System.out.println("Perform SingleRotationFromLeft(Node " + node.key + ")"); 
                } else { //subtree right-heavy (IL)
                    tree.doubleRotateFromLeft(node); 
                    System.out.println("Perform DoubleRotationFromLeft(Node " + node.key + ")");
                }
            } else { // right-heavy
                int balanceFactor_next = height(node.right.left) - height(node.right.right);
                if (balanceFactor_next > 0) { //subtree left-heavy (IR)
                    tree.doubleRotateFromRight(node); 
                    System.out.println("Perform DoubleRotationFromRight(Node " + node.key + ")"); 
                } else { //subtree right-heavy (OR)
                    tree.singleRotateFromRight(node); 
                    System.out.println("Perform SingleRotationFromRight(Node " + node.key + ")"); 
                }
            }
        }
    }

    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
        } else {
            insert(this, root, key);
        }
    }

    public static void insert(AVLTree tree, Node node, int key) {
        /*
         * หลักการ
         *  - จะทำการ rebalance ทุก node ใน path ของการ insert node ใหม่
         *  - Ex.
         *              5                           5
         *             / \                         / \
         *            2   7                       2   7
         *           /   / \         -->         /   / \
         *          1   4   9                   1   4   9
         *                 / \                         / \
         *                8   14                      8   14
         *                                                /
         *                                               10
         * 
         *    node ที่เราจะ rebalance คือ 14 , 9 , 7 , และ 5 ตามลำดับ
         */
        if (key == node.key) {
            System.out.println("Duplicated key:" + key);
        } else if (key < node.key) {// go left
            if (node.left == null) { // insert node
                node.left = new Node(key);
                node.left.parent = node;
            } else {
                insert(tree, node.left, key);
                rebalance(tree, node); // เมื่อ insert เสร็จ จึงค่อย rebalance node นี้
            }
        } else { // go right
            if (node.right == null) { // insert node
                node.right = new Node(key);
                node.right.parent = node;
            } else {
                insert(tree, node.right, key);
                rebalance(tree, node); // เมื่อ insert เสร็จ จึงค่อย rebalance node นี้
            }
        }
    }

    public void delete(int key) {
        Node del = find(key);
        // Empty tree
        if (root == null)
            System.out.println("Empty Tree!!!");
        // Key not found
        else if (del == null)
            System.out.println("Key not found!!!");
        // Key found && key == root
        else if (del == root) {
            // Root ไม่มี child
            if (root.left == null && root.right == null)
                root = null;
            // Root มี child อยู่ทางด้านซ้าย
            else if (root.left != null && root.right == null) {
                Node newRoot = root.left;
                newRoot.parent = null;
                root.left = null;
                root = newRoot;
            }
            // Root มี child อยู่ทางด้านขวา
            else if (root.left == null && root.right != null) {
                Node newRoot = root.right;
                newRoot.parent = null;
                root.right = null;
                root = newRoot;
            }
            // Root เป็น Full node
            else {
                Node min = findMin(root.right);
                int min_value = min.key;
                root.key = min_value;
                delete(this, min);
            }
        }
        // Key found && key != root
        else {
            delete(this, del);
        }
    }

    public static void delete(AVLTree tree, Node node) {

        /* หลักการ
         *  - ลบ node ตามปกติ
         *  - หลังจาก delete แล้ว เราจะทำการ rebalance ตั้งแต่ parent ของ node ที่ลบไป ถึง root
         */

        // parent ของ node ที่จะลบ
        Node current = node.parent;

        // Node ไม่มี child
        if (node.left == null && node.right == null) {
            Node par = node.parent;
            // par.key < node.key - Node อยู่ทางขวาของ Parent
            // par.key > node.key - Node อยู่ทางซ้ายของ Parent
            if (par.right != null && par.right.key == node.key)
                par.right = null;
            else
                par.left = null;
            node.parent = null;
        }
        // Node มี child อยู่ทางด้านขวา
        else if (node.left == null && node.right != null) {
            Node par = node.parent;
            Node child = node.right;
            node.parent = null;
            node.right = null;
            // par.key < node.key - child อยู่ทางขวาของ Parent
            // par.key > node.key - child อยู่ทางซ้ายของ Parent
            if (par.right != null && par.right.key == node.key)
                par.right = child;
            else
                par.left = child;
            child.parent = par;
        }
        // Node มี child อยู่ทางด้านซ้าย
        else if (node.left != null && node.right == null) {
            Node par = node.parent;
            Node child = node.left;
            node.parent = null;
            node.right = null;
            // par.key < node.key - child อยู่ทางขวาของ Parent
            // par.key > node.key - child อยู่ทางซ้ายของ Parent
            if (par.right != null && par.right.key == node.key)
                par.right = child;
            else
                par.left = child;
            child.parent = par;
        }
        // Full node
        else {
            Node min = findMin(node.right);
            int min_value = min.key;
            node.key = min_value;
            delete(tree, min);
        }

        // ทำการไล่ rebalance
        while (current != null) {
            rebalance(tree, current);
            current = current.parent;
        }
    }

    public Node find(int search_key) {
        // Pls copy the code from the previous problem
        return find(root, search_key);
    }

    public static Node find(Node node, int search_key) {
        if (node == null)
            return null;

        if (node.key == search_key)
            return node;
        else if (node.key < search_key && node.right != null)
            return find(node.right, search_key);
        else if (node.key > search_key && node.left != null)
            return find(node.left, search_key);
        else
            return null;
    }

    public static Node findMin(Node node) {
        if (node.left == null)
            return node;
        return findMin(node.left);
    }

    public static Node findMax(Node node) {
        if (node.right == null)
            return node;
        return findMax(node.right);
    }

    public static boolean isMergeable(Node r1, Node r2) {
        /*
         * หลักการ
         *   - ตรวจสอบว่า root ของ tree1 กับ root ของ tree2 สามารถ merge ได้หรือไม่
         *     โดย node สูงสุดของ tree1 จะต้องน้อยกว่า node ต่ำสุดของ tree2 
         */
        if (r1 == null || r2 == null)
            return true;
        int max_left_tree = findMax(r1).key;
        int min_right_tree = findMin(r2).key;
        return max_left_tree < min_right_tree;
    }

    public static Node mergeWithRoot(Node r1, Node r2, Node t) {
        /*
         * หลักการ
         *   - ตรวจสอบกรณีที่เป็นไปได้ 3 กรณี
         *      กรณีที่ 1      > ถ้าความสูงต่างกันไม่เกิน 1 , merge ได้ตามปกติ
         *      กรณีที่ 2,3    > ถ้าความสูงต่างกันเกิน 1
         *                      > ให้เรียก recursive ไปยังต้นไม้ที่สูงกว่า
         *                          - ถ้า tree1 สูงกว่า tree2 เรียก mergeWithRoot(r1.right, r2, t)
         *                          - ถ้า tree1 ต่ำกว่า tree2 เรียก mergeWithRoot(r1, r2.left, t) 
         *                            return root กลับมาเรียก sub_root
         *   - จากนั้น ให้ sub_root เป็น child ของ root ของ tree ที่สูงกว่า (r1 or r2 ?)
         *   - จากนั้น rebalance root ของ tree (r1 or r2)
         */
        if (isMergeable(r1, r2)) {
            if (Math.abs(height(r1) - height(r2)) <= 1) { // ถ้า tree ทั้ง 2 มีความสูงต่างกันไม่เกิน 1
                // เชื่อม 2 root ของ tree ด้วย node t
                t.left = r1; 
                if (r1 != null)
                    r1.parent = t; 
                t.right = r2; 
                if (r2 != null)
                    r2.parent = t; 
                return t; 
            } else if (height(r1) > height(r2)) { // ถ้า tree1 มีความสูงมากกว่า tree2
                // recursive หา subroot ที่ merge เรียบร้อยแล้ว 
                Node sub_root = mergeWithRoot(r1.right, r2, t);
                // นำ sub_root มาเป็น right child ของ r1
                r1.right = sub_root; 
                if (sub_root != null)
                    sub_root.parent = r1; 
                r1.parent = null;
                // จากนั้น rebalance node r1  
                // โดยเราจะสร้าง tree ใหม่ เพื่อจะใช้ rebalance() ได้ 
                AVLTree newRoot = new AVLTree(r1); 
                rebalance(newRoot, newRoot.root); 
                return newRoot.root; 
            } else if (height(r1) < height(r2)) { // ถ้า tree2 มีความสูงมากกว่า tree1
                // recursive หา subroot ที่ merge เรียบร้อยแล้ว 
                Node sub_root = mergeWithRoot(r1, r2.left, t); 
                // นำ sub_root มาเป็น left child ของ r2
                r2.left = sub_root; 
                if (sub_root != null)
                    sub_root.parent = r2; 
                r2.parent = null; 
                // จากนั้น rebalance node r2 
                // โดยเราจะสร้าง tree ใหม่ เพื่อจะใช้ rebalance() ได้ 
                AVLTree newRoot = new AVLTree(r2); 
                rebalance(newRoot, newRoot.root); 
                return newRoot.root;
            }
            return null;
        } else { // ถ้า merge ไม่ได้
            System.out.println("All nodes in T1 must be smaller than all nodes from T2");
            return r1;
        }
    }

    public void merge(AVLTree tree2) {
        /*
         * หลักการ
         *   - ตรวจสอบว่า root ของ tree1 กับ root ของ tree2 สามารถ merge ได้หรือไม่
         *     ถ้า merge ได้ 
         *      > นำ node สูงสุดของ tree1 มาเป็น node t
         *      > แล้วค่อย merge tree ทั้งสองต้นด้วย node t (mergeWithRoot(Node r1, Node r2, Node t))
         */
        if (isMergeable(this.root, tree2.root)) { // ถ้า merge ได้
            Node maxR1 = findMax(this.root);
            Node t = new Node(maxR1.key);
            delete(this, maxR1);
            root = mergeWithRoot(this.root, tree2.root, t);
        } else {
            System.out.println("All nodes in T1 must be smaller than all nodes from T2");
        }
    }

    public Node[] split(int key) {
        return split(root, key);
    }

    public Node[] split(Node r, int key) {
        /*  
         * code ตาม pseudocode ในสไลด์ 
         */
        Node[] roots = new Node[2];
        if (r == null) {
            roots[0] = roots[1] = null;
        } else if (r.key <= key) {
            roots = split(r.right, key);
            roots[0] = mergeWithRoot(r.left, roots[0], r);
        } else {
            roots = split(r.left, key);
            roots[1] = mergeWithRoot(roots[1], r.right, r);
        }
        return roots;
    }

    public static int height(Node node) {
        if (node == null)
            return -1;
        else
            return 1 + Math.max(height(node.left), height(node.right));
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Empty tree!!!");
        } else {
            super.printTree(root);
        }
    }

    public AVLTree() {
    } // Dummy Constructor, no need to edit
}