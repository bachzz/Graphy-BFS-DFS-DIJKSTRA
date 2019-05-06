package GraphWindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class IntroWindow {

	public IntroWindow() {
		// TODO Auto-generated constructor stub
		JFrame frame = new JFrame("GraphsVisual");
		JLabel textLabel = new JLabel("Graphy", SwingConstants.CENTER);
		JButton aboutBtn = new JButton("About");
		JButton simBtn = new JButton("Simulation");
		JPanel panel = new JPanel();
		ImageIcon icon = new ImageIcon(getClass().getResource("background.png"));
		JLabel mainLabel = new JLabel();

		textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
		mainLabel.setLayout(new GridBagLayout());
		mainLabel.setIcon(icon);

		aboutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                about();
            }
		});

		simBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulation();
            }
		});

		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        //gbc.weighty = 0.5;
        gbc.ipadx = 80;
        gbc.ipady = 20;
        gbc.insets = new Insets(20,0,0,0);  //top padding

        mainLabel.add(textLabel, gbc);
        gbc.gridy = 1;
		mainLabel.add(aboutBtn, gbc);
		gbc.gridy = 2;
		mainLabel.add(simBtn, gbc);

		panel.add(mainLabel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.pack();
		frame.setVisible(true);
	}

	void startSimulation() {
		GraphWindow gWin = new GraphWindow();
	}

	void about() {
		AboutWindow aWin = new AboutWindow();
	}
}
