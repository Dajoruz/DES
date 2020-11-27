package des;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
//Para el algoritmo del cifrado

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
//Para generar las subllaves
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;

public class CifradoAES {

    String llavesimetrica = "";

    private String dClave = "";
    private String dMensajeCifrado = "";
    private String dCifrado = "";
    Cipher cipher;

    public void iniciarCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            System.out.println("XDXDXD");
        }
    }

    public String CifrarAES(String llaveSimetrica, String mensaje) {
        //Generar llaves
        SecretKeySpec key = new SecretKeySpec(llaveSimetrica.getBytes(), "AES");
        //Crear objeto para cifrar

        try {
            //Crear la instancia de AES
            iniciarCipher();
            //Iniciar cifrado
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Crear arreglo de bytes del mensaje
            byte[] campoCifrado = cipher.doFinal(mensaje.getBytes());
            String base64 = new BASE64Encoder().encode(campoCifrado);
            return base64;
        } catch (Exception e) {
            System.out.println("Error al cifrar AES");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "ERROR";
        }
    }

    public String DescifrarAES(String llave, String mensaje) throws NoSuchAlgorithmException, NoSuchPaddingException {
        System.out.println("INICIANDO DESCRIFRADO");
        System.out.println("CLAVE:" + llave);
        System.out.println("MENSAJE:" + mensaje);
        SecretKeySpec key = new SecretKeySpec(llave.getBytes(), "AES");
        try {
            iniciarCipher();
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(mensaje)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("Error al descifrar AES");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "ERROR";
        }
    }

    public String AdaptarLlave(String llave) {
        String claveAdaptada = "";
        for (int i = 0; i < llave.length(); i++) {
            if (llave.charAt(i) != ' ') {
                claveAdaptada += llave.charAt(i);
            }
        }
        return claveAdaptada;
    }

    public boolean ValidarLlave(String llaveAdaptada, String cifrado) {
        int largo = llaveAdaptada.length();
        boolean valido = true;
        switch (cifrado) {
            case "DES":
                if (largo != 8) {
                    valido = false;
                }
                break;
            case "AES-128":
                if (largo != 16) {
                    valido = false;
                }
                break;
            case "AES-192":
                if (largo != 24) {
                    valido = false;
                }
                break;
            case "AES-256":
                if (largo != 32) {
                    valido = false;
                }
                break;
            default:
                valido = true;
                break;
        }
        return valido;
    }

    public void EscribirArchivo(String llave, String textoCifrado, String cifrado) throws IOException {
        File archivo = new File("Cifrado.txt");
        FileWriter escritor = new FileWriter(archivo.getName());
        escritor.append(llave + System.lineSeparator());
        escritor.append(textoCifrado + System.lineSeparator());
        escritor.append(cifrado + System.lineSeparator());
        escritor.close();
    }

    public void LeerArchivo(Path ruta) throws FileNotFoundException, IOException {
        this.dClave = Files.readAllLines(ruta).get(0);
        this.dMensajeCifrado = Files.readAllLines(ruta).get(1);
        this.dCifrado = Files.readAllLines(ruta).get(2);
    }

    public void EscribirArchivoDescifrado(String textoCifrado) throws IOException {
        File archivo = new File("Descifrado.txt");
        FileWriter escritor = new FileWriter(archivo.getName());
        escritor.append(textoCifrado + System.lineSeparator());
        escritor.close();
    }

    public String getdClave() {
        return dClave;
    }

    public void setdClave(String dClave) {
        this.dClave = dClave;
    }

    public String getdMensajeCifrado() {
        return dMensajeCifrado;
    }

    public void setdMensajeCifrado(String dMensajeCifrado) {
        this.dMensajeCifrado = dMensajeCifrado;
    }

    public String getdCifrado() {
        return dCifrado;
    }

    public void setdCifrado(String dCifrado) {
        this.dCifrado = dCifrado;
    }
}
