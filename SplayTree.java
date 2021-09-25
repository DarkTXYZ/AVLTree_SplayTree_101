import java.util.LinkedList;
import java.util.Queue;

public class SplayTree extends BTreePrinter {
    Node root;

    public SplayTree(Node root) {
        this.root = root;
        root.parent = null;
    }

    public void zig(Node x) {
        /*
         * หลักการ
         *  - เหมือน singleRotation แต่จะรับ node ลูกมา 
         *  - มี 5 กรณี
         *      > 1. ถ้า node x เป็น root จะไม่สามารถ zig ได้
         *      > 2-3. ถ้า node x เป็น child ของ root
         *          > x เป็น left child (1)
         *          > x เป็น right child (2)
         *      > 4-5. ถ้า node x ไม่เป็น child ของ root
         *          > x เป็น left child (3)
         *          > x เป็น right child (4)
         * 
         * (1)          y                   x    
         *             / \                 / \
         *            x   \     -->       /   y  
         *           / \   \             /   / \
         *          a   b   c           a   b   c
         * 
         * (2)          y                   x    
         *             / \                 / \
         *            /   x     -->       y   \  
         *           /   / \             / \   \
         *          a   b   c           a   b   c
         * 
         * (3)            w                   w
         *               /                   /
         *              y                   x    
         *             / \                 / \
         *            x   \     -->       /   y  
         *           / \   \             /   / \
         *          a   b   c           a   b   c
         * 
         * (4)            w                   w
         *               /                   /
         *              y                   y    
         *             / \                 / \
         *            /   x     -->       x   \  
         *           /   / \             / \   \
         *          a   b   c           a   b   c
         */
        Node y = x.parent;
        if (y == null) { // กรณีที่ 1
            System.out.println("Cannot perform Zig operation on the root node");
        } else if (y == root) { // กรณีที่ 2-3
            if (x.key < y.key) { // กรณีที่ 2
                // เชื่อม node
                Node B = x.right;
                y.left = B;
                if (B != null)
                    B.parent = y;
                y.parent = x;
                x.right = y;
                // set root ใหม่
                root = x;
                root.parent = null;
            }
            else { // กรณีที่ 3
                // เชื่อม node
                Node B = x.left;
                y.right = B;
                if (B != null)
                    B.parent = y;
                y.parent = x;
                x.left = y;
                // set root ใหม่
                root = x;
                root.parent = null;
            }
        } else { // กรณีที่ 4-5
            if (x.key < y.key) { // กรณีที่ 4
                Node w = y.parent;
                Node b = x.right;
                // เชื่อม node
                y.left = b;
                if (b != null)
                    b.parent = y;
                y.parent = x;
                x.right = y;
                // เชื่อม parent
                x.parent = w;
                if (w.key < x.key)
                    w.right = x;
                else
                    w.left = x;
            } else { // กรณีที่ 5
                Node w = y.parent;
                Node b = x.left;
                // เชื่อม node
                y.right = b;
                if (b != null)
                    b.parent = y;
                y.parent = x;
                x.left = y;
                // เชื่อม parent
                x.parent = w;
                if (w.key < x.key)
                    w.right = x;
                else
                    w.left = x;
            }

        }
    }

    public void zigzig(Node x) {
        /*
         * หลักการ
         *  - zig parent ก่อนแล้วค่อย zig ตัวเอง
         *  - คล้ายการ SingleRotation
         */
        zig(x.parent);
        zig(x);
    }

    public void zigzag(Node x) {
        /*
         * หลักการ
         *  - zig ตัวเอง 2 ครั้ง
         *  - คล้ายการ DoubleRotation
         */
        zig(x);
        zig(x);
    }

    public void splay(Node x) {
        /*
         * หลักการ
         *  - ถ้า x เป็น child ของ root --> zig(x)
         *  - ถ้า x เป็น grandchild และอยู่วงนอก --> zigzig(x) (1)
         *  - ถ้า x เป็น grandchild และอยู่วงใน --> zigzag(x) (2)
         *  - ทำไปเรื่อยๆ จนกว่า x == root
         * 
         * (1)            w                  x
         *               / \                / \
         *              y   d              a   y
         *             / \                    / \
         *            x   c      -->         b   w 
         *           / \                        / \
         *          a   b                      c   d
         * 
         *            w                          x
         *           / \                        / \
         *          a   y                      y   d   
         *             / \                    / \
         *            b   x      -->         w   c
         *               / \                / \
         *              c   d              a   b
         * 
         * (2)            w                  x
         *               / \                / \
         *              y   d              /   \
         *             / \                y     w
         *            a   x      -->     / \   / \   
         *               / \            a   b c   d  
         *              b   c              
         * 
         *                w                  x
         *               / \                / \
         *              a   y              /   \
         *                 / \            w     y
         *                x   d   -->    / \   / \   
         *               / \            a   b c   d  
         *              b   c              
         */
        while (x != null && x != root) {
            Node y = x.parent;
            if (y == root) { // ถ้า parent เป็น root
                zig(x);
                break;
            } else {
                Node w = y.parent;
                if ((w.key < y.key && y.key < x.key) || (x.key < y.key && y.key < w.key)) { // check ว่าเป็นวงนอกหรือไม่
                    zigzig(x);
                } else {
                    zigzag(x);
                }
            }
        }
    }

    public void insert(int key) {
        /*
         * หลักการ
         *  - insert newNode ตามปกติ
         *  - จากนั้นจึงค่อย splay newNode
         */

        // insert newNode
        Node current = root;
        Node parent = null;
        Node newNode = new Node(key);
        if (root == null) {
            root = new Node(key);
            return;
        }
        while (current != null) {
            parent = current;
            if (current.key > key)
                current = current.left;
            else
                current = current.right;
        }
        newNode.parent = parent;
        if (parent.key > key)
            parent.left = newNode;
        else
            parent.right = newNode;

        // splay newNode
        splay(newNode);
    }

    public Node find(int search_key, boolean withSplay) {
        /*
         * หลักการ
         *  - find node ตามปกติ
         *  - จากนั้นจึงค่อย splay node ที่เจอ
         */
        Node current = root;
        while (current != null) {
            if (current.key == search_key){
                if (withSplay) // ถ้าเจอ node แล้ว ค่อย splay node นั้น
                    splay(current);
                return current;
            }
            // find node
            if (current.key > search_key)
                current = current.left;
            else
                current = current.right;
        }
        return null;
    }

    public void delete(int key) {
        /*  
         *  delete(8) :
         *               10   
         *              /  \
         *             /    \
         *            5      15 
         *           / \    /  \
         *          2   8   13  20 
         *             / \
         *            6   9
         *  หลักการ
         *    > splay node ที่ต้องการจะลบขึ้นมา
         * 
         *               8   
         *              / \
         *             /   \
         *            5     10 
         *           / \    / \
         *          2   6  9   15 
         *                     / \
         *                    13  20
         *    
         *     > ลบ root ออก จะได้ 2 tree (A,B) 
         * 
         *       A (root.left)    B (root.right) 
         *            5                 10 
         *           / \                / \
         *          2   6              9   15 
         *                                 / \
         *                                13  20
         *    > splay min node ของ B
         *            A                 B  
         *            5                 9 
         *           / \                 \
         *          2   6                 10 
         *                                  \
         *                                  15
         *                                  / \
         *                                 13 20
         *    > merge tree ทั้ง 2 (B.root.left = A.root)
         *              
         *                  9
         *                 / \
         *                5   10
         *               / \    \    
         *              2   6   15
         *                      / \
         *                     13  20
         * 
         */

        Node findDel = find(key, true); // หา node แล้ว splay ขึ้นมา
        // ลบ root แล้วจะได้ 2 tree
        SplayTree A = new SplayTree(root.left);
        SplayTree B = new SplayTree(root.right);
        // splay min node ของ B
        B.splay(B.findMin());
        // เชื่อม node
        B.root.left = A.root;
        A.root.parent = B.root;
        // set root ใหม่
        root = B.root;
    }

    public Node findMin() {
        Node current = root;
        while (current.left != null)
            current = current.left;
        return current;
    }

    @SuppressWarnings("unchecked")
    public int height() {
        if (root == null)
            return -1;
        Queue<Node> q = new LinkedList();
        q.add(root);
        int height = -1;
        while (true) {
            int nodeCount = q.size();
            if (nodeCount == 0)
                return height;
            height++;
            while (nodeCount > 0) {
                Node newnode = q.remove();
                if (newnode.left != null)
                    q.add(newnode.left);
                if (newnode.right != null)
                    q.add(newnode.right);
                nodeCount--;
            }
        }
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Empty tree!!!");
        } else {
            super.printTree(root);
        }
    }

    public SplayTree() {
    }

    public static void test4() {
        BSTree2 tree1 = new BSTree2();
        long start = System.currentTimeMillis();
        int N = 40000;
        for (int i = 0; i < N; i++)
            tree1.insert(i);
        System.out.println("Time for sequentially inserting " + N + " objects into BST = "
                + (System.currentTimeMillis() - start) + " msec");
        start = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
            tree1.find((int) (Math.random() * N));

        System.out.println("Time for finding " + N + " different objects in BST= "
                + (System.currentTimeMillis() - start) + " msec");
        SplayTree tree2 = new SplayTree();
        start = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
            tree2.insert(i);

        System.out.println("Time for sequentially inserting " + N + " objects into SplayTree = "
                + (System.currentTimeMillis() - start) + " msec");
        start = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
            tree2.find((int) (Math.random() * N), true);
        System.out.println("Time for finding " + N + " different objects in SplayTree = "
                + (System.currentTimeMillis() - start) + " msec");

        System.out.println("Which one is faster: BSTree or SplayTree?");
    }

}