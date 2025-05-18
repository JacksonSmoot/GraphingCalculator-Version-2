import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import javax.swing.SwingUtilities;
public class Main {
    static final int gWidth = 1008;
    static final int screenWidth = gWidth+320; // 1280 x 800, 960 x 600, 1008 x 630 1.6
    static final int screenHeight = 830; // 0.625
    static final Color buttonGrey = new Color(97,97,97,255);
    static final Color highLightGrey = new Color(215,215,215,255);
    static int mouseOnPanel = 1;
    static boolean hideBar = false;
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Graphing Calculator");
            window.add(new Graph(new Dimension(gWidth,screenHeight), new Point(321,0), window));
            window.add(new SideBar(new Dimension(screenWidth-gWidth,screenHeight), screenWidth, window));
            window.pack();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
            window.repaint();
        });
    }

    static class SideBar extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
        final Color accentGrey = new Color(226,226,226,255);
        final Color topBarGrey = new Color(245,245,245,255);
        final Color white = new Color(255, 255, 255, 255);
        final Color numGrey = new Color(139,139,139,255);
        final Color darkGrey = new Color(44,44,44,255);
        final Color boxBorder = new Color(238,238,238,255);
        final Color boxBorderBlue = new Color(106,147,210,255);
        int numBoxes;
        int mouseX;
        int mouseY;
        Dimension dim;
        boolean hideBarFix;
        int boxHighlightX;
        JFrame window;
        int otherWidth;
        public SideBar(Dimension dim, int ow, JFrame window) {
            boxHighlightX = 2;
            this.window = window;
            numBoxes = 2;
            this.dim = dim;
            setPreferredSize(new Dimension(ow,dim.height));
            otherWidth = dim.width;
            setFocusable(true);
            addMouseListener(this);
            addMouseMotionListener(this);
            requestFocusInWindow();
        }

        public void hideSideBar(boolean hide){
            hideBar = hide;
            window.repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(!hideBar) {
                if(hideBarFix){
                    mouseY = -1000000;
                    mouseX = -1000000;
                    hideBarFix = false;
                }
                Graphics2D g2d = (Graphics2D) g;
                if (mouseOnPanel != 2) {
                    boxHighlightX = -1;
                }
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(accentGrey);
                g2d.fillRect(0, 0, dim.width, dim.height);
                g2d.setColor(white);
                g2d.fillRect(0, 0, dim.width - 1, dim.height);
                g2d.setColor(accentGrey);
                g2d.fillRect(0, 0, dim.width, 50);
                g2d.setColor(topBarGrey);
                g2d.fillRect(0, 0, dim.width - 1, 50 - 1);
                drawAddButton(g2d);
                drawBoxes(g2d, numBoxes);
                drawCButton(g2d);
            }
        }

        public void drawCButton(Graphics2D g2d) {
            if(mouseX>=268 && mouseX<=305 && mouseY>=9 && mouseY<=46) {
                g2d.setColor(highLightGrey);
                g2d.fillRoundRect(268, 6, 37, 37, 8, 8);
            }
            g2d.setColor(buttonGrey);
            g2d.fillRoundRect(280, 12, 5,25,4, 4);
            g2d.fillRoundRect(288, 12, 5,25,4, 4);
        }

        public BufferedImage rotateImage(Graphics2D g2d, BufferedImage bimg, int rotation) {

            return null;
        }

        public void drawAddButton(Graphics2D g2d) {
            int addButtonXOffsets = 18; // 6 to 49
            int addButtonYOffsets = 12;
            if(mouseX>addButtonXOffsets-6&&mouseX<49&&mouseY>addButtonYOffsets-6&&mouseY<49&&mouseY>6) {
                g2d.setColor(highLightGrey);
                g2d.fillRoundRect(addButtonXOffsets-6,addButtonYOffsets-6, 37,37,8,8);
                g2d.setColor(darkGrey);
            }
            else{
                g2d.setColor(buttonGrey);
            }
            g2d.fillRoundRect(10+addButtonXOffsets, addButtonYOffsets, 5, 25, 3, 3);
            g2d.fillRoundRect(addButtonXOffsets, 10+addButtonYOffsets, 25, 5, 3, 3);
        }

        public void drawBoxes(Graphics2D g2d, int nb) {
            Font font = new Font("Arial", Font.PLAIN, 12);
            FontMetrics metrics = g2d.getFontMetrics(font);
            g2d.setFont(font);
            for (int i = 0; i < nb; i++) {
                if (i != boxHighlightX - 1) {
                    int yOfssetValue = 51 + (58 * i);
                    g2d.setColor(accentGrey);
                    if(i==0) g2d.fillRect(0, yOfssetValue-1, dim.width, 59);
                    else g2d.fillRect(0, yOfssetValue, dim.width, 58);
                    g2d.setColor(white);
                    g2d.fillRect(40, yOfssetValue, dim.width - 41, 57);
                    g2d.setColor(boxBorder);
                    g2d.fillRect(0, yOfssetValue, 40, 57);
                    g2d.setColor(numGrey);
                    g2d.drawString(Integer.toString(i + 1), 6, yOfssetValue + 16);
                }
            }
            if (boxHighlightX != -1) {
                int yOfssetValue = 51 + (58 * (boxHighlightX - 1));
                g2d.setColor(boxBorderBlue);
                g2d.fillRect(0, yOfssetValue - 1, dim.width, 59);
                g2d.setColor(white);
                g2d.fillRect(40, yOfssetValue, dim.width - 41, 57);
                g2d.setColor(boxBorderBlue);
                g2d.fillRect(0, yOfssetValue, 40, 57);
                g2d.setColor(white);
                g2d.drawString(Integer.toString(boxHighlightX), 6, yOfssetValue + 16);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            window.repaint();
        }
        @Override public void mouseClicked(MouseEvent e) {}
        @Override public void mouseDragged(MouseEvent e) {}
        @Override public void mousePressed(MouseEvent e) {
            mouseOnPanel = 2;
            mouseX = e.getX();
            mouseY = e.getY();
            int addButtonXOffsets = 18; // 6 to 49
            int addButtonYOffsets = 12;
            boolean changeMade = false;
            if(mouseX>=268 && mouseX<=305 && mouseY>=9 && mouseY<=46) {
                hideBar = true;
                hideBarFix = true;
                window.repaint();
                return;
            }
            if(mouseX>addButtonXOffsets-6&&mouseX<49&&mouseY>addButtonYOffsets-6&&mouseY<49&&mouseY>6){
                numBoxes++;
                changeMade = true;
            }
            if (mouseY < (51 + (58 * (numBoxes))) && mouseY > 51) {
                boxHighlightX = (((mouseY - 51) - ((mouseY) % 58)) / 58) + 1;
                changeMade = true;
            }
            if (changeMade) window.repaint();
        }
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {
        }
    }

    static class Graph extends JPanel implements KeyListener, MouseMotionListener, MouseListener {

        final Color faintGrey = new Color(224, 224, 224, 255);
        final Color lightGrey = new Color(169, 169, 169, 255);
        final Color lightBlack = new Color(25, 25, 25, 255);
        final Color black = new Color(0, 0, 0, 255);
        final Color white = new Color(255, 255, 255, 255);
        final Color boxBorder = new Color(238,238,238,255);
        final Color accentGrey = new Color(226,226,226,255);
        final Color[] colorList = {faintGrey, lightGrey, lightBlack, black, white};
        final int defaultLineSpacing = 20;
        final int chunkSize = 4;
        boolean refreshGraph = false;
        boolean hideRunOne = false;
        int[][] graphGrid;
        int mouseX = 0;
        int mouseY = 0;
        int scale = 1;
        int circleWidth = 10;
        int circleHeight = 10;
        int lineSpacing = 20;
        Point correctedMouse;
        Dimension dim;
        Dimension dim2;
        JFrame window;
        boolean fixHide;
        Point graphOffsets;
        Point defaultGraphOffsets;
        public Graph(Dimension dim, Point graphOffsets, JFrame window){
            fixHide = true;
            this.window = window;
            defaultGraphOffsets = new Point(graphOffsets.x, graphOffsets.y);
            this.graphOffsets = graphOffsets;
            correctedMouse  = new Point(0, 0);
            this.dim = new Dimension(dim.width, dim.height);
            this.dim2 = new Dimension(dim.width, dim.height);
            setPreferredSize(this.dim);
            setBounds(graphOffsets.x, graphOffsets.y, this.dim.width, this.dim.height);
            setBackground(Color.WHITE);
            setFocusable(true);
            addMouseListener(this);
            addMouseMotionListener(this);
            requestFocusInWindow();
            graphGrid = makeGrid(1);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(hideBar){
                if(fixHide){
                    mouseX = -1000000;
                    mouseY = -1000000;
                    fixHide = false;
                }
                dim =  new Dimension(screenWidth, screenHeight);
                graphOffsets = new Point(0, 0);
                if(!hideRunOne) refreshGraph = true; hideRunOne = true;
            }
            else{
                dim =  new Dimension(dim2.width, dim2.height);
                graphOffsets = new Point(defaultGraphOffsets.x, defaultGraphOffsets.y);
                hideRunOne = false;
            }
            setBounds(graphOffsets.x, graphOffsets.y, this.dim.width, this.dim.height);
            if(refreshGraph){
                graphGrid = makeGrid(1);
            }
            paintGraph(g);
            if(hideRunOne){
                if(mouseX>=8 && mouseX<=45 && mouseY>=9 && mouseY<=46) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2d.setColor(highLightGrey);
                    g2d.fillRoundRect(8, 6, 37, 37, 8, 8);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
                g2d.setColor(buttonGrey);
                g2d.fillRoundRect(20, 12, 5,25,4, 4);
                g2d.fillRoundRect(28, 12, 5,25,4, 4);
            }
            drawZoomButtons(g2d);
        }

        public void drawZoomButtons(Graphics2D g2d){
            g2d.setColor(accentGrey);
            g2d.fillRoundRect(dim.width-51, 30, 42,87,4, 4);
            g2d.setColor(boxBorder);
            g2d.fillRoundRect(dim.width-50, 31, 40,85,4, 4);
            g2d.setColor(accentGrey); // 42, 73
            g2d.fillRect(dim.width-51, 73, 39,1);
            int addButtonXOffsets = 18; // 6 to 49
            int addButtonYOffsets = 12;
            g2d.setColor(buttonGrey);
            g2d.fillRoundRect(dim.width-32, 39, 4, 26, 3, 3);
        }

        public int[][] makeGrid(int layer) {
            ArrayList<int[]> lines = new ArrayList<int[]>();
            ArrayList<int[]> coloredLines = new ArrayList<int[]>();
            int x = dim.width / 2;
            int y = dim.height / 2;
            System.out.println(dim.width);
            System.out.println(dim.height);
            int correctedChunkSize = chunkSize - 1;
            int timesRun = 1;
            while ((x + (timesRun * lineSpacing)) < dim.width || (y + (timesRun * lineSpacing)) < dim.height) {
                int spacing = timesRun * lineSpacing;
                int colorInt = (timesRun % chunkSize == 0) ? 1 : 0;
                ArrayList<int[]> list = (colorInt > 0) ? coloredLines : lines;
                if (x + spacing < dim.width) {
                    // int currentLineHeight = (dim.height/2)-1;
                    list.add(new int[]{x - spacing, 0, 1, dim.height, colorInt, 1, timesRun * -1});
                    list.add(new int[]{x + spacing, 0, 1, dim.height, colorInt, 1, timesRun});
                }
                if (y + spacing < dim.height) {
                    list.add(new int[]{0, y - spacing, dim.width, 1, colorInt, 0, timesRun * -1});
                    list.add(new int[]{0, y + spacing, dim.width, 1, colorInt, 0, timesRun});
                }
                timesRun++;
            }
            int[][] grid = new int[lines.size() + coloredLines.size() + 2][5];
            for (int row = 0; row < lines.size(); row++) {
                grid[row] = lines.get(row);
            }
            int counter = 0;
            for (int row = lines.size(); row < grid.length - 2; row++) {
                grid[row] = coloredLines.get(row - lines.size());
            }
            grid[grid.length - 1] = new int[]{x - 1, 0, 2, dim.height, 2, 2};
            grid[grid.length - 2] = new int[]{0, y - 1, dim.width, 2, 2, 2};
            refreshGraph = false;
            return grid;
        }

        public void paintGraph(Graphics g){
            Font font = new Font("Arial", Font.PLAIN, 12);
            FontMetrics metrics = g.getFontMetrics(font);
            g.setFont(font);
//            int[][]  grid = makeGrid(1);
            for(int row = 0; row < graphGrid.length; row++){
                int[] line = graphGrid[row];
                g.setColor(colorList[line[4]]);
                g.fillRect(line[0],line[1],line[2],line[3]);
                if(line[5]==1&&line[4]==1){
                    int num = (line[6]/chunkSize)*scale;
                    String numString = Integer.toString(num);
                    int numStringWidth = metrics.stringWidth(numString);
                    int numStringAscent = metrics.getAscent();
                    int yOffsets = (lineSpacing)-(numStringAscent/2);
                    g.setColor(white);
                    g.fillRect(line[0]-(lineSpacing/2),(((dim.height / 2) + yOffsets)-numStringAscent)+1,lineSpacing,metrics.getAscent());
                    g.setColor(black);
                    if(num>0) {
                        g.drawString(numString, line[0] - (numStringWidth / 2), (dim.height / 2) + yOffsets);
                    }
                    else{
                        int negLength = numStringWidth-metrics.stringWidth(Integer.toString(Math.abs(num)));
                        int xOffsets = metrics.stringWidth(Integer.toString(Math.abs(num)))/2;
                        g.drawString(numString, (line[0] - xOffsets)-negLength, (dim.height / 2) + yOffsets);
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override public void mouseDragged(MouseEvent e) {}
        @Override public void mousePressed(MouseEvent e) {
            if(hideBar) {
                if (mouseX >= 8 && mouseX <= 45 && mouseY >= 9 && mouseY <= 46) {
                    refreshGraph = true;
                    hideBar = false;
                    fixHide = true;
                }
            }
            mouseOnPanel = 1;
            window.repaint();
        }
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }
}