package encodagecbc;

public class CBC {
    private String msg, Vecteur_init, Alphabet;
    private char[] alphaTab;
    private String[] M;

    public CBC(){
        new CBC(" ", " ");
    }

    public CBC(String message, String VecteurInit){
        Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,'?! ";
        alphaTab = Alphabet.toCharArray();
        Vecteur_init = VecteurInit.toUpperCase();
        msg = message.toUpperCase();
        int r = msg.length()/VecteurInit.length();
        if (msg.length()%Vecteur_init.length() != 0)
            r = r + 1;
        M = new String[r];
        Init();
    }

    public String getMsg(){
        return msg;
    }
    public String getVecteur_init(){
        return Vecteur_init;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public void setVecteur_init(String Vecteur_init){
        this.Vecteur_init = Vecteur_init;
    }

    private void Init(){
        for(int i = 0; i < msg.length()%Vecteur_init.length(); i++)
                msg += " ";
        for (char c : msg.toCharArray()) { // remplacement des caractères inconnus à lalphabet
            if(!Alphabet.contains(""+c))
                msg = msg.replace(c, alphaTab[29]);
        }
        for (char c : Vecteur_init.toCharArray()) { // remplacement des caractères inconnus à lalphabet
            if(!Alphabet.contains(""+c))
                Vecteur_init = Vecteur_init.replace(c, alphaTab[29]);
        }
        for(int i = 0; i < M.length; i++){ // Remplissage de la matrice
           M[i] = (msg.substring(i * Vecteur_init.length(), Vecteur_init.length() * (i+1)));
        }
    }

    public int[] Substitution_LC(String s){
        int[] substitut = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            substitut[i] = (Alphabet.indexOf(s.toUpperCase().toCharArray()[i]));
        }
        return substitut;
    }

    public String Substitution_CL(int[] A){
        String tmp = "";
        for (int i = 0; i < A.length; i++) {
            tmp += Alphabet.toCharArray()[A[i]];
        }
        return tmp;
    }

    public int[] Permutation(int[] tab){  // Ou plutot un reneversement
        int[] tmp = new int[tab.length];
        for(int i = 0; i < tmp.length; i++)
            tmp[i] = tab[(tab.length - 1) - i];
        return tmp;
    }

    public String[] Encodage(){
        String Vi = Vecteur_init;
/* ****************** Phase de substitution ***********************/
        int[][] Ms = new int[M.length][Vi.length()];
        int j = 0;
        for (String s : M) {
            Ms[j] = Substitution_LC(s);
            j = j + 1;
        }
        int[] Vis = Substitution_LC(Vi);

/* *************** Phase de Conversion en Binaire ******************************/
        BCDConverter conv = new BCDConverter();

        int[][] Vic = new int[Vis.length][5];
        for (int i = 0; i < Vis.length; i++) {
            conv.setN(Vis[i]);
            Vic[i] = conv.DecToBin();
        }
        int[][][] Mc = new int[Ms.length][Vis.length][5];
        for(int a = 0; a < Ms.length; a++){
            for (int b = 0; b < Ms[a].length; b++) {
                conv.setN(Ms[a][b]);
                Mc[a][b] = conv.DecToBin();
            }
        }

/* ************************* Phase du Ou Exclusif et de la permutation ****************************************** */
        BoolOP op = new BoolOP();
        int[][][] E = new int[Mc.length][Vic.length][5];
        for(int a = 0; a < Mc.length; a++){
            for(int b = 0; b < Vic.length; b++){
                E[a][b] = op.Big_XOR(Mc[a][b], Vic[b]);
                E[a][b] = Permutation(E[a][b]);
            }
            for(int k = 0; k < Vic.length; k++)  // On change la valeur du vecteur initial pour la diffusion
                Vic[k] = E[a][k];
        }

/* ************************ Phase de conversion en décimal ************************************/
        int[][] Ec = new int[E.length][Vic.length];
        for(int a = 0; a < E.length; a++){
            for (int b = 0; b < Vic.length; b++) {
                Ec[a][b] = conv.BinToDec(E[a][b]);
            }
        }

/* **************************** Phase de Substitution inverse ***********************************************/
        String[] sortie = new String[Ec.length];
        for(int a = 0; a < sortie.length; a++){
            sortie[a] = Substitution_CL(Ec[a]);
        }
        // sortie test
        //affichage3(E);
        //System.out.print("\n");
        //affichage2(Ec);
        return sortie;
    }

    public String Decodage(String[] msgCrypte){
        Init();
        String msgClair = "";
        BCDConverter conv = new BCDConverter();
        BoolOP op = new BoolOP();
        int[][] D = new int[msgCrypte.length][Vecteur_init.length()];
/* **************** Substitution  *********************************/
        for(int i = 0; i < D.length; i++){
            D[i] = Substitution_LC(msgCrypte[i]);
        }
        int[] Vis = Substitution_LC(Vecteur_init);
/* ****************** Opérations de convertion Dec-Bin  ***************************/
        int[][][] Dc = new int[D.length][Vecteur_init.length()][5];
        for(int i = 0; i < Dc.length; i++){
            for(int j = 0; j < Vecteur_init.length(); j++) {
                conv.setN(D[i][j]);
                Dc[i][j] = conv.DecToBin();
            }
        }
        int[][] Vic = new int[Vis.length][5];
        for (int i = 0; i < Vic.length; i++) {
            conv.setN(Vis[i]);
            Vic[i] = conv.DecToBin();
        }
/* ********************* Permutation **********************************/
        int[][][] Dp = new int[D.length][Vecteur_init.length()][5];
        for (int i = 0; i < Dp.length; i++) {
            for(int j = 0; j < Dp[i].length; j++)
                Dp[i][j] = Permutation(Dc[i][j]);
        }
/* ******************** Xor et diffusion *************************************/
        int [][][] Di = new int[Dc.length][Vecteur_init.length()][5];
        for(int i = 0; i < Di.length; i++){
            for(int j = 0; j < Di[i].length; j++){
                Di[i][j] = op.Big_XOR(Dp[i][j], Vic[j]);
            }
            Vic = Dc[i];
        }
/* ****************** Convertion Bin-Dec ******************************/
        int[][] Dic = new int[Di.length][Di[0].length];
        for(int i = 0; i < Dic.length; i++){
            for (int j = 0; j < Dic[i].length; j++)
                Dic[i][j] = conv.BinToDec(Di[i][j]);
        }
/* ****************** Substitution inverse ***********************/
        for (int[] k : Dic)
            msgClair += Substitution_CL(k);

        return msgClair;
    }

    public void affichage3(int[][][] T){
        for (int[][] k : T) {
            for (int[] l : k) {
                for (int m : l) {
                    System.out.print(m);
                }
                System.out.print(".");
            }
            System.out.print(" ");
        }
    }
    public void affichage2(int[][] T){
        for (int[] k : T) {
            for ( int l : k) {
                System.out.print(l+".");
            }
            System.out.print(" ");
        }
    }
}
