package part2;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Program {
	public static void main(String[] args) {
		JFrame frame=new JFrame("Free money");
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		JButton button=new JButton("Click me for money");
		button.setBounds(150, 150, 250, 50);
		var button2=new JButton("Check the console for your bank account :)");
		button.setBounds(150, 100, 250, 50);

		final int[] money={0};

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				money[0]+=100;
				JOptionPane.showMessageDialog(frame,"+100 money");
				System.out.println("Total money: "+money[0]);
			}
		});

		frame.add(button);
		frame.setVisible(true);
	}
}