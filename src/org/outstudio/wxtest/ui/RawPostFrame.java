package org.outstudio.wxtest.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.outstudio.wxtest.util.XmlUtil;

public class RawPostFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private MainFrame mainFrame;

	private JPanel contentPane;
	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;
	private JButton btnSendRaw;

	/**
	 * Launch the application.
	 */
	public static void launch(final MainFrame parent) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RawPostFrame frame = new RawPostFrame(parent);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RawPostFrame(MainFrame parent) {
		setTitle("\u8F93\u5165XML \u6216 \u4ECEtxt\u6587\u4EF6\u4E2D\u9009\u62E9");
		this.mainFrame = parent;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

		JButton btnSelectFromFile = new JButton("\u9009\u62E9txt\u6587\u4EF6");
		btnSelectFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obBtnSelectFileClicked();
			}
		});
		panel.add(btnSelectFromFile);

		btnSendRaw = new JButton("\u53D1\u9001");
		btnSendRaw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onBtnSendRawClicked();
			}
		});
		panel.add(btnSendRaw);

		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		textArea.setCodeFoldingEnabled(true);

		scrollPane = new RTextScrollPane(textArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	}

	private void obBtnSelectFileClicked() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {// 只能选择txt文件
			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".txt");
			}
		});
		fc.showOpenDialog(this);
		File file = fc.getSelectedFile();
		if (!file.getName().endsWith(".txt")) {
			JOptionPane.showMessageDialog(this, "请选择txt文件！");
			return;
		}
		try {
			String content = FileUtils.readFileToString(file);
			textArea.setText(content);
			boolean isValidXml = XmlUtil.isValidXml(content);
			if (!isValidXml) {
				JOptionPane.showMessageDialog(this, "不符合XML格式！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onBtnSendRawClicked() {
		String xml = textArea.getText();
		boolean isValidXml = XmlUtil.isValidXml(xml);
		if (xml.isEmpty() || !isValidXml) {
			JOptionPane.showMessageDialog(this, "不符合XML格式！");
			return;
		}
		mainFrame.sendXml(xml);
	}

}
