public class BSTree extends BTreePrinter {
    Node root;

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

    public Node find(int search_key) {
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

    public void insert(int key) {
        if (root == null) {
            Node newNode = new Node(key);
            root = newNode;
        } else
            insert(root, key);
    }

    public static void insert(Node node, int key) {
        Node current = node;
        Node prev = null;

        while (current != null) {
            prev = current;
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            }
        }

        Node Parent = prev;
        Node Child = new Node(key);

        Child.parent = Parent;
        if (Parent.key > key)
            Parent.left = Child;
        else
            Parent.right = Child;

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
                delete(min);
                root.key = min_value;
            }
        }
        // Key found && key != root
        else {
            delete(del);
        }
    }

    public static void delete(Node node) {
        if (node.left == null && node.right == null) {
            Node par = node.parent;
            node.parent = null;
            // par.key < node.key - Node อยู่ทางขวาของ Parent
            // par.key > node.key - Node อยู่ทางซ้ายของ Parent
            if (par.key < node.key)
                par.right = null;
            else
                par.left = null;
        }
        // Node มี child อยู่ทางด้านขวา
        else if (node.left == null && node.right != null) {
            Node par = node.parent;
            Node child = node.right;
            node.parent = null;
            node.right = null;
            // par.key < node.key - child อยู่ทางขวาของ Parent
            // par.key > node.key - child อยู่ทางซ้ายของ Parent
            if (par.key < node.key)
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
            if (par.key < node.key)
                par.right = child;
            else
                par.left = child;
            child.parent = par;
        }
        // Full node
        else {
            Node min = findMin(node.right);
            int min_value = min.key;
            delete(min);
            node.key = min_value;
        }
    }

    public static boolean isMergeable(Node r1, Node r2) {
        /*
         * หลักการ
         *   - ตรวจสอบว่า root ของ tree1 กับ root ของ tree2 สามารถ merge ได้หรือไม่
         *     โดย node สูงสุดของ tree1 จะต้องน้อยกว่า node ต่ำสุดของ tree2 
         */
        Node maxR1 = findMax(r1);
        Node minR2 = findMin(r2);
        return maxR1.key < minR2.key;
    }

    public static Node mergeWithRoot(Node r1, Node r2, Node t) {
        /*
         * หลักการ
         *   - ตรวจสอบว่า root ของ tree1 กับ root ของ tree2 สามารถ merge ได้หรือไม่
         *     ถ้า merge ได้ 
         *      > root ของ tree1 เป็น left child ของ t
         *      > root ของ tree2 เป็น right child ของ t
         */
        if (isMergeable(r1, r2)) {
            t.left = r1;
            t.right = r2;
            if (r1.parent != null)
                r1.parent = t;
            if (r2.parent != null)
                r2.parent = t;
            return t;
        } else {
            System.out.println("All nodes in T1 must be smaller than all nodes from T2");
            return r1;
        }
    }

    public void merge(BSTree tree2) {
        /*
         * หลักการ
         *   - ตรวจสอบว่า root ของ tree1 กับ root ของ tree2 สามารถ merge ได้หรือไม่
         *     ถ้า merge ได้ 
         *      > นำ node สูงสุดของ tree1 มาเป็น node t
         *      > แล้วค่อย merge tree ทั้งสองต้นด้วย node t (mergeWithRoot(Node r1, Node r2, Node t))
         */
        if (isMergeable(this.root, tree2.root)) {
            Node maxR1 = findMax(this.root);
            Node t = new Node(maxR1.key);
            delete(maxR1);
            this.root = mergeWithRoot(this.root, tree2.root, t);
        } else {
            System.out.println("All nodes in T1 must be smaller than all nodes from T2");
        }
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Empty tree!!!");
        } else {
            super.printTree(root);
        }
    }
}