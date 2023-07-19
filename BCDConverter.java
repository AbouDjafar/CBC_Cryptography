package encodagecbc;

public class BCDConverter {
    private int n;

    public BCDConverter(int n) {
        this.n = n;
    }

    public BCDConverter() {
        n = 0;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public String decToBin() {
        if (n == 0)
            return "00000";
        else {
            String s = "";
            int d = n;
            while (d > 0) {
                s = d % 2 + s;
                d /= 2;
            }
            switch (s.length()) {
                case (1):
                    s = "0000" + s;
                    break;
                case (2):
                    s = "000" + s;
                    break;
                case (3):
                    s = "00" + s;
                    break;
                case (4):
                    s = "0" + s;
                    break;
            }
            return (s);
        }
    }

    public int[] DecToBin(){
        String s = Integer.toBinaryString(n);
        int t = s.length();
        int[] o = new int[t];
        if(t < 5) {
            o = new int[5];
            for(int i = 0; i < (5 - t) - 1; i++)
                o[i] = 0;
            for(int i = 0; i < s.length(); i++)
                o[i + (5 - t)] = Integer.parseInt(""+s.toCharArray()[i]);
        }else{
            for(int i = 0; i < s.length(); i++)
                o[i] = Integer.parseInt(""+s.toCharArray()[i]);
        }

        return o;
    }

    public int binToDec(String s){
        char[] tab = s.toCharArray();
        int nbr = 0;
        if ((s.contains("0") == true || s.contains("1") == true)){
            for(int i = 0; i < tab.length; i++)
                nbr += Math.pow(2,i) * Integer.parseInt(""+tab[(tab.length - 1) - i]);
        }
        return nbr;
    }

    public int BinToDec(int[] b){
        int o = 0;
        for (int i = 0; i < b.length; i++) {
            o += Math.pow(2, i) * b[(b.length - 1) - i];
        }
        return o;
    }
}

