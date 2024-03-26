import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class ImageIFMG extends JFrame {
  private JDesktopPane theDesktop;
  private int[][] matR, matG, matB;

  JFileChooser fc = new JFileChooser();
  String path = "";

  public void escalaCinza() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();

    // percorrer linhas da matriz
    for (int l = 0; l < matrizVermelha.length; l++) {
      // percorre colunas
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        int media = (matrizVermelha[l][c] + matrizVerde[l][c] +
            matrizAzul[l][c]) / 3;
        matrizVermelha[l][c] = media;
        matrizVerde[l][c] = media;
        matrizAzul[l][c] = media;
      }
    }

    geraImagem(matrizVermelha, matrizVerde, matrizAzul);

  }

  public void imagemBinaria() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();
    int meio = 127;
    for (int l = 0; l < matrizVermelha.length; l++) {
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        int media = (matrizVermelha[l][c] + matrizVerde[l][c]
            + matrizAzul[l][c]) / 3;
        if (media >= meio) {
          matrizVermelha[l][c] = 255;
          matrizVerde[l][c] = 255;
          matrizAzul[l][c] = 255;
        } else {
          matrizVermelha[l][c] = 0;
          matrizVerde[l][c] = 0;
          matrizAzul[l][c] = 0;
        }
      }
    }

    geraImagem(matrizVermelha, matrizVerde, matrizAzul);
  }

  public void imagemNegativa() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();
    int maxColorValue = 255;

    for (int l = 0; l < matrizVermelha.length; l++) {
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        matrizVermelha[l][c] = maxColorValue - matrizVermelha[l][c];
        matrizVerde[l][c] = maxColorValue - matrizVerde[l][c];
        matrizAzul[l][c] = maxColorValue - matrizAzul[l][c];
      }
    }

    geraImagem(matrizVermelha, matrizVerde, matrizAzul);
  }

  public void corDominante() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();
    int maxColorValue = 255;

    for (int l = 0; l < matrizVermelha.length; l++) {
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        int valorVermelho = matrizVermelha[l][c];
        int valorVerde = matrizVerde[l][c];
        int valorAzul = matrizAzul[l][c];

        int valorTotal = valorVermelho + valorVerde + valorAzul;
        if (valorTotal > 0) {
          valorVermelho = (valorVermelho * maxColorValue) / valorTotal;
          valorVerde = (valorVerde * maxColorValue) / valorTotal;
          valorAzul = (valorAzul * maxColorValue) / valorTotal;
        }

        matrizVermelha[l][c] = valorVermelho;
        matrizVerde[l][c] = valorVerde;
        matrizAzul[l][c] = valorAzul;
      }
    }

    geraImagem(matrizVermelha, matrizVerde, matrizAzul);
  }

  public void escalaCinzaEscuro() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();
    for (int l = 0; l < matrizVermelha.length; l++) {
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        int menor = Math.min(matrizVermelha[l][c], Math.min(matrizVerde[l][c], matrizAzul[l][c]));
        matrizVermelha[l][c] = menor;
        matrizVerde[l][c] = menor;
        matrizAzul[l][c] = menor;
      }
    }

    geraImagem(matrizVermelha, matrizVerde, matrizAzul);
  }

  public void escalaCinzaClaro() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();
    for (int l = 0; l < matrizVermelha.length; l++) {
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        int maior = Math.max(matrizVermelha[l][c], Math.max(matrizVerde[l][c], matrizAzul[l][c]));
        matrizVermelha[l][c] = maior;
        matrizVerde[l][c] = maior;
        matrizAzul[l][c] = maior;
      }

    }

    geraImagem(matrizVermelha, matrizVerde, matrizAzul);
  }

  public void rgbToHsv() {
    int[][] matrizVermelha = obterMatrizVermelha();
    int[][] matrizVerde = obterMatrizVerde();
    int[][] matrizAzul = obterMatrizAzul();
    int[][] valor = new int[matrizVermelha.length][matrizVermelha[0].length];
    int[][] saturacao = new int[matrizVermelha.length][matrizVermelha[0].length];
    int[][] hue = new int[matrizVermelha.length][matrizVermelha[0].length];

    for (int l = 0; l < matrizVermelha.length; l++) {
      for (int c = 0; c < matrizVermelha[0].length; c++) {
        int vermelho = matrizVermelha[l][c];
        int verde = matrizVerde[l][c];
        int azul = matrizAzul[l][c];
        int maior = Math.max(vermelho, Math.max(verde, azul));
        int menor = Math.min(vermelho, Math.min(verde, azul));
        int diff = maior - menor;
        valor[l][c] = maior;
        saturacao[l][c] = maior > 0 ? (maior - menor)/maior : 0;
        if(maior == vermelho) {
          hue[l][c] = diff > 0 ? 60*(verde - azul)/(maior - menor) : 0;
        } else if (maior == verde) {
          hue[l][c] = diff > 0 ? 60*(2 + azul - vermelho)/(maior - menor) : 0;
        } else {
          hue[l][c] = diff > 0 ? 60*(4 + vermelho - verde)/(maior - menor) : 0;
        }
        if(hue[l][c] < 0) hue[l][c] += 360;
      }

    }
    geraImagem(hue, saturacao, valor);
  }

  public ImageIFMG() {
    super("PhotoIFMG");
    JMenuBar bar = new JMenuBar();
    JMenu addMenu = new JMenu("Abrir");
    JMenuItem fileItem = new JMenuItem("Abir uma imagem de arquivo");
    JMenuItem newFrame = new JMenuItem("Internal Frame");

    addMenu.add(fileItem);
    addMenu.add(newFrame);
    bar.add(addMenu);

    JMenu addMenu2 = new JMenu("Processar");
    JMenuItem item1 = new JMenuItem("Escala de cinza");
    JMenuItem item2 = new JMenuItem("Imagem binária");
    JMenuItem item3 = new JMenuItem("Negativa");
    JMenuItem item4 = new JMenuItem("Cor dominante");
    JMenuItem item5 = new JMenuItem("Cinza escuro");
    JMenuItem item6 = new JMenuItem("Cinza claro");
    JMenuItem item7 = new JMenuItem("To HSV");

    addMenu2.add(item1);
    addMenu2.add(item2);
    addMenu2.add(item3);
    addMenu2.add(item4);
    addMenu2.add(item5);
    addMenu2.add(item6);
    addMenu2.add(item7);

    bar.add(addMenu2);

    setJMenuBar(bar);

    theDesktop = new JDesktopPane();
    getContentPane().add(theDesktop);

    newFrame.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            JInternalFrame frame = new JInternalFrame("Exemplo", true,
                true, true, true);
            Container container = frame.getContentPane();
            MyJPanel panel = new MyJPanel();
            container.add(panel, BorderLayout.CENTER);

            frame.pack();
            theDesktop.add(frame);
            frame.setVisible(true);

          }
        }

    );

    // ler imagem
    fileItem.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.CANCEL_OPTION) {
              return;
            }
            path = fc.getSelectedFile().getAbsolutePath();

            JInternalFrame frame = new JInternalFrame("Exemplo", true,
                true, true, true);
            Container container = frame.getContentPane();
            MyJPanel panel = new MyJPanel();
            container.add(panel, BorderLayout.CENTER);

            frame.pack();
            theDesktop.add(frame);
            frame.setVisible(true);
          }
        }

    );

    item1.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);
            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            escalaCinza();

            /*
             * int[][] mat = rgbMat.elementAt(0);
             * int[][] mat2 = rgbMat.elementAt(1);
             * int[][] mat3 = rgbMat.elementAt(2);
             * 
             * geraImagem(mat3, mat2
             * , mat);
             */
          }
        }

    );

    item2.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);

            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            imagemBinaria();

          }
        }

    );

    item3.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);

            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            imagemNegativa();
          }
        }

    );

    item4.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);

            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            corDominante();
          }
        }

    );

    item5.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);

            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            escalaCinzaEscuro();
          }
        }

    );

    item6.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);

            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            escalaCinzaClaro();

          }
        }

    );

    item7.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Vector<int[][]> rgbMat = getMatrixRGB();
            matR = rgbMat.elementAt(0);

            matG = rgbMat.elementAt(1);
            matB = rgbMat.elementAt(2);

            rgbToHsv();
          }
        }

    );

    setSize(600, 400);
    setVisible(true);

  }

  // ler matrizes da imagem
  public Vector<int[][]> getMatrixRGB() {
    BufferedImage img;
    int[][] rmat = null;
    int[][] gmat = null;
    int[][] bmat = null;
    try {
      img = ImageIO.read(new File(path));

      int[][] pixelData = new int[img.getHeight() * img.getWidth()][3];
      rmat = new int[img.getHeight()][img.getWidth()];
      gmat = new int[img.getHeight()][img.getWidth()];
      bmat = new int[img.getHeight()][img.getWidth()];

      int counter = 0;
      for (int i = 0; i < img.getHeight(); i++) {
        for (int j = 0; j < img.getWidth(); j++) {
          rmat[i][j] = getPixelData(img, j, i)[0];
          gmat[i][j] = getPixelData(img, j, i)[1];
          bmat[i][j] = getPixelData(img, j, i)[2];

          /*
           * for(int k = 0; k < rgb.length; k++){
           * pixelData[counter][k] = rgb[k];
           * }
           */

          counter++;
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    Vector<int[][]> rgb = new Vector<int[][]>();
    rgb.add(rmat);
    rgb.add(gmat);
    rgb.add(bmat);

    return rgb;

  }

  // cria imagem da matriz
  private void geraImagem(int matrix1[][], int matrix2[][], int matrix3[][]) {
    int[] pixels = new int[matrix1.length * matrix1[0].length * 3];
    BufferedImage image = new BufferedImage(matrix1[0].length, matrix1.length, BufferedImage.TYPE_INT_RGB);
    WritableRaster raster = image.getRaster();
    int pos = 0;
    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[0].length; j++) {
        pixels[pos] = matrix1[i][j];
        pixels[pos + 1] = matrix2[i][j];
        pixels[pos + 2] = matrix3[i][j];
        pos += 3;
      }
    }
    raster.setPixels(0, 0, matrix1[0].length, matrix1.length, pixels);

    // Abre Janela da imagem
    JInternalFrame frame = new JInternalFrame("Processada", true,
        true, true, true);
    Container container = frame.getContentPane();
    MyJPanel panel = new MyJPanel();
    panel.setImageIcon(new ImageIcon(image));
    container.add(panel, BorderLayout.CENTER);

    frame.pack();
    theDesktop.add(frame);
    frame.setVisible(true);

  }

  public int[][] obterMatrizVermelha() {
    return matR;
  }

  public int[][] obterMatrizVerde() {
    return matG;
  }

  public int[][] obterMatrizAzul() {
    return matB;
  }

  private static int[] getPixelData(BufferedImage img, int x, int y) {
    int argb = img.getRGB(x, y);

    int rgb[] = new int[] {
        (argb >> 16) & 0xff, // red
        (argb >> 8) & 0xff, // green
        (argb) & 0xff // blue
    };

    return rgb;
  }

  class MyJPanel extends JPanel {
    private ImageIcon imageIcon;

    public MyJPanel() {
      imageIcon = new ImageIcon(path);
    }

    public void setImageIcon(ImageIcon i) {
      imageIcon = i;
    }

    public void paintComponent(Graphics g) {
      super.paintComponents(g);
      imageIcon.paintIcon(this, g, 0, 0);
    }

    public Dimension getPreferredSize() {
      return new Dimension(imageIcon.getIconWidth(),
          imageIcon.getIconHeight());
    }

  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    ImageIFMG app = new ImageIFMG();
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

}
