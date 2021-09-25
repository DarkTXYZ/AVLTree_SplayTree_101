public class BSTree2 extends BTreePrinter{
    Node root;
    
    public Node find(int search_key){
        /*
         * หลักการ
         *  - ให้เริ่มต้นที่ root (current = root)
         *  - จากนั้น check เงื่อนไข
         *      > ถ้า search_key มากกว่า current.key ให้ลงไปยัง sub-tree ทางขวา (current = current.right)
         *      > ถ้า search_key น้อยกว่า current.key ให้ลงไปยัง sub-tree ทางซ้าย (current = current.left)
         *  - วนไปเรื่อยๆ จนกว่า current.key == search.key จึงค่อย return node นั้นออกมา
         */
        Node current = root;
        while(current != null){
            if(current.key == search_key)
                return current;
            if(current.key > search_key)
                current = current.left;
            else
                current = current.right;
        }
        return current;
    }

    public Node findMin(){
        /*
         * หลักการ
         *  - node ที่น้อยที่สุดจะอยู่ทางซ้ายสุด
         *  - ให้เรื่มต้นที่ root
         *  - ให้ลงไป subtree ทางซ้ายเรื่อยๆ (current = current.left) จนกว่าจะไปไม่ได้ (current.left != null)
         *  - แล้วจึง return node นั้น
         */
        Node current = root;
        while(current.left != null)
            current = current.left;
        return current; 
    }

    public Node findMax(){
        /*
         * หลักการ
         *  - node ที่มากที่สุดจะอยู่ทางขวาสุด
         *  - ให้เรื่มต้นที่ root
         *  - ให้ลงไป subtree ทางขวาเรื่อยๆ (current = current.right) จนกว่าจะไปไม่ได้ (current.right != null)
         *  - แล้วจึง return node นั้น
         */
        Node current = root;
        while(current.right != null)
            current = current.right;
        return current; 
    }
    
    public void insert(int key) {
        /*
         * หลักการ
         *  - ถ้าไม่มี root ให้ newNode เป็น root เลย
         *  - ถ้ามี หา node ที่จะเติม newNode เข้าไป วิธีการหาเหมือน find()
         *  - จากนั้นจึงให้ newNode เป็น child ของ node นั้น (parent)
         */

        Node current = root;
        Node parent = null;
        Node newNode = new Node(key);
        // ถ้าไม่มี root
        if(root == null){
            root = new Node(key);
            return ;
        }
        
        // ถ้ามี หา node ที่จะเติม
        // มี parent ไว้คอยช่วยในการหา
        while(current != null){
            parent = current;
            if(current.key > key)
                current = current.left;
            else
                current = current.right;
        }
        
        // เชื่อม newNode กับ parent
        newNode.parent = parent;
        if(parent.key > key)
            parent.left = newNode;
        else
            parent.right = newNode;
        
    }
    
    public void printTree() {
        if (root == null) {
            System.out.println("Empty tree!!!");
        } else {
            super.printTree(root);
        }
    }
}