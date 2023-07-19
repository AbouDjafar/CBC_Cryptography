package encodagecbc;

public class BoolOP {
    public char XOR(char a, char b){
       if (a == b)
           return '0';
        else
            return '1';
    }
    public int XOR(int a, int b){
        return (a == b) ? 0 : 1;
    }

    public String Big_XOR(String a, String b){
        String C = a;
        if(a.length() == b.length()){
            for (int i = 0; i < a.length(); i++){
                C.toCharArray()[i] = XOR(a.toCharArray()[i], b.toCharArray()[i]);
            }
        }else
            System.out.println("Les argument n'ont pas les même longueurs");

        return C;
    }
    public int[] Big_XOR(int[] A, int[] B){
        int[] out = new int[A.length];
        if(A.length == B.length){
            for (int i = 0; i < out.length; i++) {
                out[i] = XOR(A[i], B[i]);
            }
        }
        return out;
    }
}
