package mmk.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class App {

	public static void main(String[] args) {
		
		String image1 = pdf2Jpeg("cover-3");
		String image2 = pdf2Jpeg("cover-4");
		imageComparing(image1, image2);
		
		System.out.println("Completed");
		
	}
	private static String pdf2Jpeg(String fileName) {
		try {
			System.out.println("PDF2JPEG has been started.");
		    PDDocument document = PDDocument.load(new File("src/main/resources/pdf/" + fileName + ".pdf"));
		    PDFRenderer pdfRenderer = new PDFRenderer(document);
		    for (int page = 0; page < document.getNumberOfPages(); ++page) {
		        BufferedImage bim = pdfRenderer.renderImageWithDPI(
		          page, 600, ImageType.RGB);
		        ImageIOUtil.writeImage(
		          bim, "src/main/resources/image/" + fileName + "-" + (page + 1) + ".jpg", 600);
		    }
		    document.close();
		    System.out.println("PDF2JPEG has been completed.");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return fileName + "-1.jpg";
		
	}
	private static void imageComparing(String imageFirst, String imageSecond) {
		try {
			BufferedImage img1 = ImageIO.read(new File("src/main/resources/image/" + imageFirst));
			BufferedImage img2 = ImageIO.read(new File("src/main/resources/image/" + imageSecond));
			
			int w1 = img1.getWidth();
			int w2 = img2.getWidth();
			int h1 = img1.getHeight();
			int h2 = img2.getHeight();

			if(w1 != w2 || h1 != h2) {
				System.out.println("They are not the similar images");
				return;
			}
			
			BufferedImage imgf = new BufferedImage(w1, h1, img1.getType());
			
			for (int j = 0; j < h1; j++) {
				for (int i = 0; i < w1; i++) {
					int pixel1 = img1.getRGB(i, j);
					int pixel2 = img2.getRGB(i, j);
					   
					if(pixel1 == pixel2)
						imgf.setRGB(i, j, pixel1);
					else {
						if(pixel2 == Color.WHITE.getRGB()) {
							// blue for removed lines
							Color c = new Color(pixel1);
							imgf.setRGB(i, j, new Color(c.getRed(), c.getGreen(), 255).getRGB());
						}
						else {
							//red for added lines
							Color c = new Color(pixel2);
							imgf.setRGB(i, j, new Color(255, c.getGreen(), c.getBlue()).getRGB());
						}
					}
	            }
			}
			System.out.println(ImageIO.write(imgf, "jpg", new File("src/main/resources/image/compared.jpg")));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
