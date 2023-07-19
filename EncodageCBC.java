package encodagecbc;

import java.util.Scanner;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EncodageCBC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String m, v, r;
        //m = "";
        Path source;
        FileWriter fw = null;
        FileReader fr = null;
        CBC crypto;        
        System.out.println( "**********        ********            **********\n" +
                "**********        **********          **********\n" +
                "**                **       ****       **\n" +
                "**                **********          **\n" +
                "**                **       ***        **\n" +
                "**                **       ******     **\n" +
                "**********        *************       **********\n" +
                "**********        *********           ********** \n");
        do{
            m = "";
            System.out.println(" +++++++++ MENU +++++++ \n" +
                    "1. Encoder un bout de texte [taper 1] \n2. Decoder un texte code selon le CBC [taper 2]\n3. A propos de moi [taper 3]\n  Quitter [taper q]" +
                    "\nVotre choix: ");
            r = sc.nextLine();
            if(r.equals("1")){
                System.out.println("Veuillez saisir un texte a encoder: ");
                m = sc.nextLine();
                if(m.isEmpty())
                    m = "vide";
                System.out.println("Veuillez saisir un mot secret quelconque: ");
                v = sc.nextLine();
                if (v.isEmpty())
                    v = "vide";
                crypto = new CBC(m, v);
                String[] msgEnc = crypto.Encodage();
                System.out.println("Veuillez saisir le nom du fichier dans lequel sera sauvegardé votre message codé: ");
                String fn = sc.nextLine();        
                
                // enregistrement dans un fichier
                try{
                    if(!Files.isDirectory(Paths.get(System.getProperty("user.dir")+"/Encode")))
                        Files.createDirectory(Paths.get(System.getProperty("user.dir")+"/Encode"));
                    source = Paths.get(System.getProperty("user.dir")+"/Encode/"+fn+".txt");                   
                    while(source.getFileName() == null || Paths.get(source.toUri()).toFile().exists()){
                        System.out.println("nom de fichier invalide ou preexistant, veuillez resaisir un autre nom:");
                        fn = sc.nextLine();
                        source = Paths.get(System.getProperty("user.dir")+"/Encode/"+fn+".txt");
                    }
                    fw = new FileWriter(new File(source.toString()));
                    fw.write(v +"_");
                    System.out.print("Votre message codé est le suivant: ");
                    for(String s : msgEnc){
                        System.out.print(s);
                        fw.write(s);
                    }
                    fw.close();
                    System.out.println("  .Il est enregistré dans ce fichier: "+source.getFileName().toString());
                }catch(IOException e){ e.printStackTrace();};
            }
            if (r.equals("2")){                            
                System.out.println("Veuillez saisir le nom du fichier (sans l'extension) contenant le message encode.\n Assurez-vous que ce fichier se trouve dans le dossier "+System.getProperty("user.dir")+"\\Encode");
                String nf = sc.nextLine();
                try{
                    if(!Files.isDirectory(Paths.get(System.getProperty("user.dir")+"/Decode")))
                        Files.createDirectory(Paths.get(System.getProperty("user.dir")+"/Decode"));
                    source = Paths.get(System.getProperty("user.dir")+"/Encode/"+nf+".txt");                    
                    if(!Paths.get(source.toUri()).toFile().exists())
                        System.out.println("Fichier inexistant");
                    else{
                        fr = new FileReader(new File(source.toString()));                       
                       int c = 0;
                       while((c = fr.read()) != -1)
                           m += (char)c;
                       fr.close();
                    }
                }catch(IOException e){ e.printStackTrace();};     
                String[] tabM = m.split("_");
                if(tabM.length != 2)
                    System.out.println("Erreur: Fichier corrompu");
                else{
                    if(tabM[1].isEmpty())
                        tabM[1] = "vide";
                    v = tabM[0];
                    if (v.isEmpty())
                        v = "vide";
                    int n = (tabM[1].length()%v.length() == 0) ? tabM[1].length()/v.length() : (tabM[1].length()/v.length()) + 1;
                    String[] m_dec = new String[n];
                    for(int i = 0; i < n; i++) {
                        if (((i + 1) * v.length()) > tabM[1].length())
                            m_dec[i] = tabM[1].substring(i * v.length(), tabM[1].length());
                        else
                            m_dec[i] = tabM[1].substring(i * v.length(), (i + 1) * v.length());
                    }
                    crypto = new CBC(" ", v);                    
                    String ss = crypto.Decodage(m_dec);                    
                    source = Paths.get(System.getProperty("user.dir")+"/Decode/"+nf+"-decode"+System.nanoTime()+".txt");
                    try{
                        fw = new FileWriter(new File(source.toString()));
                        fw.write(ss);
                        fw.close();
                    }catch (IOException e){ e.printStackTrace();};
                    System.out.println("Le message ainsi decode est: "+ss
                    +"\n Il est enregistre dans "+source.getFileName().toString());
                }
            }
            if (r.equals("3")){
                System.out.println("All done by him => ('_')... and Djaf (^_^)");
            }
        }while(!r.equals("q"));         
    }
}
