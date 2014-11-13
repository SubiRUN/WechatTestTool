package org.outstudio.wxtest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.easywechat.msg.BaseMsg;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jsoup.nodes.Document;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField tfText;
	private JTextField tfToken;
	private JComboBox<String> cbUrl;

	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	private JEditorPane epView;
	private RTextScrollPane scrollPane_1;
	private RSyntaxTextArea rawResp;
	private JLabel lblTime;

	private List<SiteInfo> siteInfos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("微信测试工具");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel topPane = new JPanel();
		contentPane.add(topPane, BorderLayout.NORTH);
		topPane.setLayout(null);
		topPane.setPreferredSize(new Dimension(getWidth(), 100));

		cbUrl = new JComboBox<String>();
		cbUrl.setEditable(true);
		cbUrl.setBounds(74, 10, 270, 21);
		cbUrl.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				loadSiteInfo();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		cbUrl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String url = getUrlFromInput();
				for (SiteInfo siteInfo : siteInfos) {
					if (siteInfo.getUrl().equals(url)) {
						displayToken(siteInfo.getToken());
					}
				}
			}
		});
		topPane.add(cbUrl);

		JLabel lblNewLabel = new JLabel("\u9009\u62E9\u7F51\u5740\uFF1A");
		lblNewLabel.setBounds(10, 13, 64, 15);
		topPane.add(lblNewLabel);

		tfText = new JTextField();
		tfText.setBounds(10, 41, 269, 21);
		topPane.add(tfText);
		tfText.setColumns(10);

		JButton btnSendText = new JButton("\u53D1\u9001\u6587\u5B57");
		btnSendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onBtnSendClicked();
			}
		});
		btnSendText.setBounds(289, 41, 93, 23);
		topPane.add(btnSendText);

		JLabel lblToken = new JLabel("token\uFF1A");
		lblToken.setBounds(374, 13, 54, 15);
		topPane.add(lblToken);

		tfToken = new JTextField();
		tfToken.setBounds(428, 10, 76, 21);
		topPane.add(tfToken);
		tfToken.setColumns(10);

		lblTime = new JLabel("");
		lblTime.setBounds(10, 72, 119, 15);
		topPane.add(lblTime);

		JButton btnMenuClick = new JButton("\u70B9\u51FB\u83DC\u5355");
		btnMenuClick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onBtnMenuClicked();
			}
		});
		btnMenuClick.setBounds(392, 41, 93, 23);
		topPane.add(btnMenuClick);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.45);
		contentPane.add(splitPane, BorderLayout.CENTER);

		scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		epView = new JEditorPane();
		scrollPane.setViewportView(epView);
		splitPane.setResizeWeight(0.5);

		rawResp = new RSyntaxTextArea();
		rawResp.setEditable(false);
		rawResp.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		rawResp.setCodeFoldingEnabled(true);

		scrollPane_1 = new RTextScrollPane(rawResp);
		splitPane.setRightComponent(scrollPane_1);

		loadSiteInfo();
	}

	/**
	 * 从配置文件中加载网址和token
	 */
	private void loadSiteInfo() {
		cbUrl.removeAllItems();
		siteInfos = SiteInfoUtil.getSiteInfo();
		for (SiteInfo siteInfo : siteInfos) {
			String url = siteInfo.getUrl();
			cbUrl.addItem(url);
		}
		cbUrl.repaint();
	}

	private void onBtnSendClicked() {
		displayFormattedResp("正在等待服务器响应！");

		String rawUrl = getUrlFromInput();
		String token = tfToken.getText();
		String text = tfText.getText();

		if (!rawUrl.startsWith("http")) {
			rawUrl = "http://" + rawUrl;
		}
		String resp;
		try {
			long beginTime = System.currentTimeMillis();
			resp = WechatUtil.sendText(token, rawUrl, text);
			long endTime = System.currentTimeMillis();
			displayDelayTime(endTime - beginTime);
		} catch (IllegalArgumentException ee) {
			displayErrorMsg("网址格式有误！");
			return;
		} catch (IOException e) {
			displayErrorMsg("出现IO错误！");
			return;
		}

		parseAndDisplay(resp);
		saveSiteInfo(new SiteInfo(rawUrl, token));
	}

	private void onBtnMenuClicked() {
		displayFormattedResp("正在等待服务器响应！");

		String rawUrl = getUrlFromInput();
		String token = tfToken.getText();
		String eventKey = tfText.getText();

		if (!rawUrl.startsWith("http")) {
			rawUrl = "http://" + rawUrl;
		}
		String resp;
		try {
			long beginTime = System.currentTimeMillis();
			resp = WechatUtil.sendClickEvent(token, rawUrl, eventKey);
			long endTime = System.currentTimeMillis();
			displayDelayTime(endTime - beginTime);
		} catch (IllegalArgumentException ee) {
			displayErrorMsg("网址格式有误！");
			return;
		} catch (IOException e) {
			displayErrorMsg("出现IO错误！");
			return;
		}

		parseAndDisplay(resp);
		saveSiteInfo(new SiteInfo(rawUrl, token));
	}

	/**
	 * 解析返回的消息并进行展示
	 */
	private void parseAndDisplay(String resp) {
		if (resp.trim().isEmpty()) {
			displayErrorMsg("返回为空！");
			return;
		}

		Document doc = WechatUtil.getDocFromXml(resp);

		displayRawRespText(doc.toString());

		BaseMsg msg;
		try {
			msg = WechatUtil.getMsgFromDoc(doc);
		} catch (Exception e) {
			displayFormattedResp("解析出现异常！");
			return;
		}

		// 将解析后的消息展现到界面中
		String formattedResp = WechatUtil.getPrettyString(msg);
		displayFormattedResp(formattedResp);
	}

	/**
	 * 将原先不存在的服务器地址和token存进配置文件
	 */
	private void saveSiteInfo(SiteInfo info) {
		String url = info.getUrl();
		String token = info.getToken();

		boolean hasUrl = false;
		for (SiteInfo siteInfo : siteInfos) {
			if (siteInfo.getUrl().equals(url)) {
				hasUrl = true;
				break;
			}
		}
		if (!hasUrl) {
			siteInfos.add(new SiteInfo(url, token));
			SiteInfoUtil.saveSiteInfo(siteInfos);
		}
	}

	private String getUrlFromInput() {
		return cbUrl.getEditor().getItem().toString();
	}

	private void displayToken(final String token) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				tfToken.setText(token);
			}
		});
	}

	private void displayDelayTime(final long timeInMillis) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				lblTime.setText("用时：" + timeInMillis / 1000d + " 秒");
			}
		});
	}

	private void displayErrorMsg(final String errMsg) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				epView.setText(errMsg);
				rawResp.setText("");
			}
		});
	}

	private void displayFormattedResp(final String text) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				epView.setText(text);
			}
		});
	}

	private void displayRawRespText(final String text) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				rawResp.setText(text);
			}
		});
	}
}
