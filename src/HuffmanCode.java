

import java.util.*;

class HuffmanNode
{
    Character ch;
    Integer freq;
    HuffmanNode left = null;
    HuffmanNode right = null;
    HuffmanNode(Character ch, Integer freq)
    {
        this.ch = ch;
        this.freq = freq;
    }
    public HuffmanNode(Character ch, Integer freq, HuffmanNode left, HuffmanNode right)
    {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

//Main class
public class HuffmanCode
{
    public static void createHuffmanTree(String text)
    {
        if (text == null || text.length() == 0)
        {
            return;
        }
        Map<Character, Integer> freq = new HashMap<>();
        for (char c: text.toCharArray())
        {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));
        // Hang doi ưu tiên, cho các kí tự theo tần suất xuất hiện, xuất hiện ít nhất thì ở cuối hàng đợi
        for (var entry: freq.entrySet())
        {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        System.out.println("---------------------------------");
        System.out.println("\n\n");
        // Tạo cây huffman, gộp các phần tử của hàng đợi ưu tiên thành phần tử chung có thuộc tính trái và phải
        //pq: hàng đợi luuw các kí tự theo tần suất
        while (pq.size() != 1) // pq nhiều phần tử
        {
            HuffmanNode left = pq.poll(); //khi poll thì hàng đợi sẽ xóa luôn kí tự đó, poll 2 lần thì phần tử ấy mất
            HuffmanNode right = pq.poll();
            //sau khi poll thì sẽ mất 2 phần tử xác suất nhỏ nhất
            int sum = left.freq + right.freq;
            //Lưu tần suất của left và right
            //adding a new internal node (deleted nodes i.e. right and left) to the queue with a frequency that is equal to the sum of both nodes
            pq.add(new HuffmanNode(null, sum, left, right));
            //Add 1 phaanf tử vào hàng đợi mà có tần suất là tổng của 2 node con và có 2 thuộc tính left và right tương ứng 2 node con
        }

        //root stores pointer to the root of Huffman Tree  
        HuffmanNode root = pq.peek();
        // Hàng đợi có phần tử lớn nhất là phần tử 1 trên đỉnh
        //trace over the Huffman tree and store the Huffman codes in a map  
        Map<Character, String> huffmanCode = new HashMap<>(); // Lưu kis tự và xâu mã hóa
        encodeData(root, "", huffmanCode);


        //print the Huffman codes for the characters  
        System.out.println("Huffman Codes of the characters are: " + huffmanCode);
//        for (char c: text.toCharArray()){
//            System.out.print(c);
//            System.out.println(huffmanCode.get(c));
//        }

            //prints the initial data
//        System.out.println("The initial string is: " + text);
//        StringBuilder sb = new StringBuilder();
//        for (char c: text.toCharArray())
//        {
//            sb.append(huffmanCode.get(c));
//        }
//        System.out.println("The encoded string is: " + sb);

        // Tính ENTROPY mã hóa và chiều dài mã TB
        int L = text.length();
        double avg = 0.0;
        double e = 0.0;
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            Character key = entry.getKey(); //Key của phần tử đang xét
            double value = (double) entry.getValue()/L; // Tần suất
            e -= value * Math.log(value)/Math.log(2);
            if(huffmanCode.containsKey(key)){
                int lg = huffmanCode.get(key).toString().length(); //Getkey: lấy xâu mã hóa dựa trên kí tự key r tính chiều dài lg
                avg += lg*value;
            }
        }
        System.out.println("Entropy: " + (double)Math.round(e * 100)/100 + "(bits)");
        System.out.println("Chiều dài mã trung bình: " + (double)Math.round(avg * 100)/100);
        System.out.println("Hiệu suất mã hóa: " + (double)Math.round(e/avg * 10000)/100 + "%");
        System.out.println("Tính dư thừa: " + (double)Math.round((1.0 - e/avg )*10000)/100 + "%");

    }

    public static void encodeData(HuffmanNode root, String str, Map<Character, String> huffmanCode)
    {
        if (root == null)
        {
            return;
        }
        //checks if the node is a leaf node or nnt
        if (isLeaf(root)) //Kiểm tra node hiện tại có phải là lá (nhánh cuối cùng)
        {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        encodeData(root.left, str + '0', huffmanCode);
        encodeData(root.right, str + '1', huffmanCode);
    }

    //function to check if the Huffman Tree contains a single node
    public static boolean isLeaf(HuffmanNode root)
    {
        return root.left == null && root.right == null;
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("String for Huffman encoding: \n");
        String inputString = scanner.nextLine();
        scanner.close();
        createHuffmanTree(inputString);
    }
}  