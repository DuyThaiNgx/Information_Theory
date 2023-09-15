import java.util.*;

class ShannonNode {
    char character;
    int frequency;
    String code; //Lưu xâu mã hóa vào
    ShannonNode left;
    ShannonNode right;

    public ShannonNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.code = "";
    }
}

public class ShannonCode {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("String for Shannon encoding: \n");
        String inputString = scanner.nextLine();
        scanner.close();

        // Tạo hashmap chứa ký tự và tần xuất
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : inputString.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        // Lưu dãy các shannon nodes
        List<ShannonNode> nodes = new ArrayList<>();
        for (char c : frequencyMap.keySet()) {
            nodes.add(new ShannonNode(c, frequencyMap.get(c)));
        }

        Collections.sort(nodes, new Comparator<ShannonNode>() {
            @Override
            public int compare(ShannonNode node1, ShannonNode node2) {
                // Compare the frequencies in reverse order
                return Integer.compare(node2.frequency, node1.frequency);
            }
        });
        //Tạo cây shannon
        ShannonNode root = buildShannonFanoTree(nodes);
        buildShannonFanoCodes(root);

        System.out.println("---------------------------------");
        System.out.println("\n\n");
        System.out.println("Shannon-Fano Encoding:");
        for (ShannonNode node : nodes) {
            System.out.println(node.character + " : " + node.code);
        }

        // Tính ENTROPY mã hóa và chiều dài mã TB

        int L = inputString.length();
        double avg = 0.0;
        double e = 0.0;

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            Character key = entry.getKey();
            double value = (double) entry.getValue()/L;
            e -= value * Math.log(value)/Math.log(2);
            for (ShannonNode node : nodes) {
                if(node.character == key){
                    avg += node.code.toString().length() * value;
                }
            }
        }
        System.out.println("Entropy: " + (double)Math.round(e * 100)/100 + "(bits)");
        System.out.println("Chiều dài mã trung bình: " + (double)Math.round(avg * 100)/100);
        System.out.println("Hiệu suất mã hóa: " + (double)Math.round(e/avg * 10000)/100 + "%");
        System.out.println("Tính dư thừa: " + (double)Math.round((1 - e/avg )*10000)/100 + "%");

    }

    private static ShannonNode buildShannonFanoTree(List<ShannonNode> nodes) {
        if (nodes.size() <= 1) {
            return nodes.get(0);
        }
        //Tạo ra cây
        int totalFrequency = 0;
        for (ShannonNode node : nodes) {
            totalFrequency += node.frequency;
        }

        int splitIndex = findSplitIndex(nodes, totalFrequency);
        //Sublist: truyền vào 2 giá tr
        List<ShannonNode> leftNodes = nodes.subList(0, splitIndex);
        List<ShannonNode> rightNodes = nodes.subList(splitIndex, nodes.size());

        ShannonNode left = buildShannonFanoTree(leftNodes);
        ShannonNode right = buildShannonFanoTree(rightNodes);

        ShannonNode root = new ShannonNode('\0', totalFrequency);
        root.left = left;
        root.right = right;

        return root;
    }

    private static int findSplitIndex(List<ShannonNode> nodes, int totalFrequency) {
        int splitIndex = 1; // Giá trị để cắt arrayList. khởi tạo giá trị
        int currentFrequency = nodes.get(0).frequency; //Tần suất của kí tự đầu tiên trong node VD : 2
        int difference = Math.abs(totalFrequency - 2 * currentFrequency); //giá trị chênh lệch của totalFr và curFr
        //TotalFr: tần suất tổng của node VD: 12
        //Nodesize: số phần tử của node
        while (splitIndex < nodes.size() - 1) { // duyệt từ đầu đến cuối nốt
            currentFrequency += nodes.get(splitIndex).frequency; // = tần suất từ phần tử splitIndex trở về phần tử thứ 0 của mảng
            int currentDifference = Math.abs(totalFrequency - 2 * currentFrequency);
            if (currentDifference < difference) { //Tìm thấy 1 phần tử có su chênh lệch tần số gần hơn, + splitIndex và cập nhật giá trị difference
                difference = currentDifference;
                splitIndex++; //Phần tử trong mảng
            } else {
                break;
            }
        }

        return splitIndex;
    }

    private static void buildShannonFanoCodes(ShannonNode node) {
        if (node == null) {
            return;
        }

        if (node.left != null) {
            node.left.code = node.code + "0";
            buildShannonFanoCodes(node.left);
        }

        if (node.right != null) {
            node.right.code = node.code + "1";
            buildShannonFanoCodes(node.right);
        }
    }

    private static int calculateEncodedLength(String inputString, List<ShannonNode> nodes) {
        int length = 0;
        for (char c : inputString.toCharArray()) {
            for (ShannonNode node : nodes) {
                if (node.character == c) {
                    length += node.code.length();
                    break;
                }
            }
        }
        return length;
    }
}
