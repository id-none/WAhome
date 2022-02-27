import java.util.Arrays;

public class Test {
    public String largestNumber(int[] nums) {
        StringBuilder s = new StringBuilder();
        for (int i : nums) {
            s.append(i);
        }
        int l = s.length();
        int[] array = new int[l];
        for (int i = 0; i < l; i++) {
            array[i] = Integer.parseInt(s.charAt(i) + "");
        }
        Arrays.sort(array);
        String re = "";
        for (int i = l - 1; i >= 0; i--) {
            re += array[i];
        }
        return re;
    }

    public boolean isPalindrome(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char i : chars) {
            if ((i >= 'a' && i <= 'z') || (i>='0' && i <='9')) {
                stringBuilder.append(i);
            }
        }
        s = stringBuilder.toString();
        return s.equals(stringBuilder.reverse().toString());
    }
}
