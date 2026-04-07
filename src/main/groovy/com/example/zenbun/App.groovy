package com.example.zenbun

import javax.swing.*
import java.awt.*
import java.awt.event.*
import javax.swing.filechooser.FileNameExtensionFilter
import java.util.List
import java.util.ArrayList

class App extends JFrame {

    private JTextField rootFolderField = new JTextField()
    private JTextField keywordField = new JTextField()
    private JCheckBox regexCheckBox = new JCheckBox('正規表現')
    private JCheckBox ignoreCaseCheckBox = new JCheckBox('大文字小文字を無視')
    
    private JTextArea resultArea = new JTextArea()
    private JLabel resultCountLabel = new JLabel('検索結果件数: 0')
    
    private TextSearchEngine searchEngine = new TextSearchEngine()
    private ConfigManager configManager = new ConfigManager()
    private KeywordHistory keywordHistory = new KeywordHistory()

    App() {
        setupUI()
        setupEventHandlers()
        loadInitialState()
    }

    private void setupUI() {
        setTitle('Zenbun - テキスト検索ツール')
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setSize(800, 600)
        setLocationRelativeTo(null)
        
        // メインレイアウト
        setLayout(new BorderLayout())
        
        // 検索条件指定ペイン（上半分）
        JPanel searchPane = createSearchPane()
        
        // 検索結果表示ペイン（下半分）
        JPanel resultPane = createResultPane()
        
        // 分割ペインで上下に配置
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        splitPane.setTopComponent(searchPane)
        splitPane.setBottomComponent(resultPane)
        splitPane.setDividerLocation(0.5)
        splitPane.setResizeWeight(0.5)
        
        add(splitPane, BorderLayout.CENTER)
    }

    private JPanel createSearchPane() {
        JPanel pane = new JPanel()
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS))
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))
        
        // ルートフォルダ選択行
        JPanel folderBox = new JPanel(new FlowLayout(FlowLayout.LEFT))
        folderBox.add(new JLabel('ルートフォルダ:'))
        rootFolderField.setPreferredSize(new Dimension(400, 25))
        folderBox.add(rootFolderField)
        JButton folderButton = new JButton('フォルダ指定')
        folderButton.addActionListener({ selectRootFolder() })
        folderBox.add(folderButton)
        pane.add(folderBox)
        
        // 検索キーワード行
        JPanel keywordBox = new JPanel(new FlowLayout(FlowLayout.LEFT))
        keywordBox.add(new JLabel('検索キーワード:'))
        keywordField.setPreferredSize(new Dimension(400, 25))
        keywordBox.add(keywordField)
        JButton historyButton = new JButton('履歴')
        historyButton.addActionListener({ showKeywordHistory() })
        keywordBox.add(historyButton)
        pane.add(keywordBox)
        
        // 検索オプション行
        JPanel optionBox = new JPanel(new FlowLayout(FlowLayout.LEFT))
        optionBox.add(regexCheckBox)
        optionBox.add(ignoreCaseCheckBox)
        pane.add(optionBox)
        
        // 検索ボタン行
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.LEFT))
        JButton searchButton = new JButton('検索')
        searchButton.setPreferredSize(new Dimension(100, 25))
        searchButton.addActionListener({ performSearch() })
        buttonBox.add(searchButton)
        pane.add(buttonBox)
        
        return pane
    }

    private JPanel createResultPane() {
        JPanel pane = new JPanel()
        pane.setLayout(new BorderLayout())
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))
        
        pane.add(resultCountLabel, BorderLayout.NORTH)
        
        resultArea.setEditable(false)
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12))
        JScrollPane scrollPane = new JScrollPane(resultArea)
        scrollPane.setPreferredSize(new Dimension(800, 300))
        pane.add(scrollPane, BorderLayout.CENTER)
        
        return pane
    }

    private void setupEventHandlers() {
        // 検索結果のダブルクリック処理
        resultArea.addMouseListener(new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleResultDoubleClick()
                }
            }
        })
    }

    private void loadInitialState() {
        rootFolderField.setText(keywordHistory.getLastRootFolder())
        regexCheckBox.setSelected(keywordHistory.isLastRegexEnabled())
        ignoreCaseCheckBox.setSelected(keywordHistory.isLastIgnoreCaseEnabled())
    }

    private void selectRootFolder() {
        JFileChooser chooser = new JFileChooser()
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
        chooser.setDialogTitle('フォルダを選択')
        
        if (rootFolderField.getText() && new File(rootFolderField.getText()).exists()) {
            chooser.setCurrentDirectory(new File(rootFolderField.getText()))
        }
        
        int result = chooser.showOpenDialog(this)
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile()
            rootFolderField.setText(selectedFile.getAbsolutePath())
        }
    }

    private void showKeywordHistory() {
        JDialog historyDialog = new JDialog(this, '検索キーワード履歴', true)
        historyDialog.setSize(400, 300)
        historyDialog.setLocationRelativeTo(this)
        
        JList<String> listView = new JList<>(keywordHistory.getAllKeywords().toArray(new String[0]))
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        
        listView.addMouseListener(new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = listView.getSelectedValue()
                    if (selected) {
                        keywordField.setText(selected)
                        historyDialog.dispose()
                    }
                }
            }
        })
        
        JScrollPane scrollPane = new JScrollPane(listView)
        historyDialog.add(scrollPane)
        historyDialog.setVisible(true)
    }

    private void performSearch() {
        String rootFolder = rootFolderField.getText()
        String keyword = keywordField.getText()
        boolean useRegex = regexCheckBox.isSelected()
        boolean ignoreCase = ignoreCaseCheckBox.isSelected()
        
        if (!rootFolder || !keyword) {
            resultArea.setText('ルートフォルダと検索キーワードを指定してください。')
            return
        }
        
        keywordHistory.addKeyword(keyword)
        keywordHistory.setLastRootFolder(rootFolder)
        keywordHistory.setLastRegexEnabled(useRegex)
        keywordHistory.setLastIgnoreCaseEnabled(ignoreCase)
        
        try {
            ArrayList<SearchResult> results = searchEngine.search(rootFolder, keyword, useRegex, ignoreCase)
            displayResults(results, rootFolder)
        } catch (Exception e) {
            resultArea.setText("エラーが発生しました: ${e.message}")
        }
    }

    private void displayResults(ArrayList<SearchResult> results, String rootFolder) {
        resultCountLabel.setText("検索結果件数: ${results.size()}")
        
        StringBuilder sb = new StringBuilder()
        results.each { result ->
            String relativePath = new File(rootFolder).toPath().relativize(new File(result.filePath).toPath()).toString()
            sb.append("${relativePath}:${result.lineNo}:${result.columnNo}\n")
            sb.append("${result.line}\n")
            sb.append("\n")
        }
        
        resultArea.setText(sb.toString())
        resultArea.setCaretPosition(0) // スクロールを先頭に
    }

    private void handleResultDoubleClick() {
        try {
            String selectedText = resultArea.getSelectedText()
            if (!selectedText) return
            
            // 選択された行からファイルパスと行番号を抽出
            String[] lines = resultArea.getText().split('\n')
            int caretPos = resultArea.getCaretPosition()
            int lineStart = resultArea.getLineOfOffset(caretPos)
            
            if (lineStart < lines.length) {
                String line = lines[lineStart]
                def matcher = line =~ /^(.+?):(\d+):(\d+)/
                if (matcher) {
                    String relativePath = matcher[0][1]
                    int lineNo = matcher[0][2] as int
                    
                    String absolutePath = new File(rootFolderField.getText(), relativePath).getAbsolutePath()
                    String command = configManager.interpolateCommand(absolutePath, lineNo)
                    
                    // コマンド実行
                    Runtime.runtime.exec(command)
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "コマンド実行エラー: ${e.message}", "エラー", JOptionPane.ERROR_MESSAGE)
        }
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater({
            new App().setVisible(true)
        })
    }
}
