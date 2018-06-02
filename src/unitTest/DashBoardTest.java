package unitTest;

import java.awt.BorderLayout;
import java.util.TooManyListenersException;

import javax.swing.JFrame;

import dashBoard.DashBoardController;
import dashBoard.DashBoardModel;
import device.SerialTool;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import utils.SwingUtils;

public class DashBoardTest implements SerialPortEventListener {
	
	public static void main(String[] args) throws TooManyListenersException {
		DashBoardTest t = new DashBoardTest();
		SerialTool.getInstance().addEventListener(t);
		
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DashBoardController controller = new DashBoardController(new DashBoardModel());
		frame.getContentPane().add(SwingUtils.createPanelForComponent(controller.getView(), "DashBoard"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				System.out.println(new String(SerialTool.getInstance().read()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
