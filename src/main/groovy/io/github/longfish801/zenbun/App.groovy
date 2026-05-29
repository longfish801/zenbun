/*
 * App.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */
package io.github.longfish801.zenbun

import groovy.util.logging.Slf4j
import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import java.awt.event.*
import javax.swing.filechooser.FileNameExtensionFilter
import java.util.List
import java.util.ArrayList

@Slf4j('LOG')
class App extends JFrame {

    private JTextField rootFolderField = new JTextField()
    private JTextField keywordField = new JTextField()
    private JCheckBox regexCheckBox = new JCheckBox('正規表現')
    private JCheckBox ignoreCaseCheckBox = new JCheckBox('大文字小文字を無視')
    
    private JTable resultTable = new JTable()
    private JLabel resultCountLabel = new JLabel('検索結果件数: 0')
    
    private TextSearchEngine searchEngine = new TextSearchEngine()
    private ConfigManager configManager = new ConfigManager()
    private SavedInput savedInput = new SavedInput()

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
        JButton folderHistoryButton = new JButton('フォルダ履歴')
        folderHistoryButton.addActionListener({ showFolderHistory() })
        folderBox.add(folderHistoryButton)
        JButton clearFolderHistoryButton = new JButton('フォルダ履歴消去')
        clearFolderHistoryButton.addActionListener({ clearFolderHistory() })
        folderBox.add(clearFolderHistoryButton)
        pane.add(folderBox)
        
        // 検索キーワード行
        JPanel keywordBox = new JPanel(new FlowLayout(FlowLayout.LEFT))
        keywordBox.add(new JLabel('検索キーワード:'))
        keywordField.setPreferredSize(new Dimension(400, 25))
        keywordBox.add(keywordField)
        JButton historyButton = new JButton('履歴')
        historyButton.addActionListener({ showKeywordHistory() })
        keywordBox.add(historyButton)
        JButton clearHistoryButton = new JButton('履歴消去')
        clearHistoryButton.addActionListener({ clearKeywordHistory() })
        keywordBox.add(clearHistoryButton)
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
        
        // テーブルモデルの作成
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            boolean isCellEditable(int row, int column) {
                return false  // 全セルを編集不可に設定
            }
        }
        model.addColumn("ファイルパス")
        model.addColumn("行番号")
        model.addColumn("列番号")
        model.addColumn("行内容")
        
        // テーブルの設定
        resultTable.setModel(model)
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF)
        resultTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11))
        resultTable.setRowHeight(25)
        
        // ダブルクリック処理の追加
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleTableDoubleClick()
                }
            }
        })
        
        JScrollPane scrollPane = new JScrollPane(resultTable)
        scrollPane.setPreferredSize(new Dimension(800, 300))
        pane.add(scrollPane, BorderLayout.CENTER)
        
        return pane
    }

    private void setupEventHandlers() {
        keywordField.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "performSearch"
        )
        keywordField.getActionMap().put("performSearch", new AbstractAction() {
            @Override
            void actionPerformed(ActionEvent e) {
                performSearch()
            }
        })
    }

    private void loadInitialState() {
        rootFolderField.setText(savedInput.getLastRootFolder())
        regexCheckBox.setSelected(savedInput.isLastRegexEnabled())
        ignoreCaseCheckBox.setSelected(savedInput.isLastIgnoreCaseEnabled())
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
            String folderPath = selectedFile.getAbsolutePath()
            rootFolderField.setText(folderPath)
            savedInput.addFolder(folderPath)
        }
    }

    private void showKeywordHistory() {
        JDialog historyDialog = new JDialog(this, '検索キーワード履歴', true)
        historyDialog.setSize(400, 300)
        historyDialog.setLocationRelativeTo(this)
        
        JList<String> listView = new JList<>(savedInput.getAllKeywords().toArray(new String[0]))
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

    private void showFolderHistory() {
        JDialog folderHistoryDialog = new JDialog(this, 'フォルダ履歴', true)
        folderHistoryDialog.setSize(400, 300)
        folderHistoryDialog.setLocationRelativeTo(this)
        
        JList<String> listView = new JList<>(savedInput.getAllFolders().toArray(new String[0]))
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        
        listView.addMouseListener(new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = listView.getSelectedValue()
                    if (selected) {
                        rootFolderField.setText(selected)
                        folderHistoryDialog.dispose()
                    }
                }
            }
        })
        
        JScrollPane scrollPane = new JScrollPane(listView)
        folderHistoryDialog.add(scrollPane)
        folderHistoryDialog.setVisible(true)
    }

    private void clearKeywordHistory() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            '履歴を消去しますか？',
            '履歴消去の確認',
            JOptionPane.YES_NO_OPTION
        )
        if (answer == JOptionPane.YES_OPTION) {
            savedInput.clearKeywords()
            JOptionPane.showMessageDialog(this, '検索キーワードの履歴を消去しました。', '履歴消去', JOptionPane.INFORMATION_MESSAGE)
        }
    }

    private void clearFolderHistory() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            'ルートフォルダの履歴を消去しますか？',
            'フォルダ履歴消去の確認',
            JOptionPane.YES_NO_OPTION
        )
        if (answer == JOptionPane.YES_OPTION) {
            savedInput.clearFolders()
            JOptionPane.showMessageDialog(this, 'ルートフォルダの履歴を消去しました。', 'フォルダ履歴消去', JOptionPane.INFORMATION_MESSAGE)
        }
    }

    private void performSearch() {
        String rootFolder = rootFolderField.getText()
        String keyword = keywordField.getText()
        boolean useRegex = regexCheckBox.isSelected()
        boolean ignoreCase = ignoreCaseCheckBox.isSelected()
        
        if (!rootFolder || !keyword) {
            JOptionPane.showMessageDialog(this, 'ルートフォルダと検索キーワードを指定してください。', 'エラー', JOptionPane.ERROR_MESSAGE)
            return
        }
        
        savedInput.addKeyword(keyword)
        savedInput.setLastRootFolder(rootFolder)
        savedInput.setLastRegexEnabled(useRegex)
        savedInput.setLastIgnoreCaseEnabled(ignoreCase)
        
        try {
            ArrayList<SearchResult> results = searchEngine.search(rootFolder, keyword, useRegex, ignoreCase)
            displayResults(results, rootFolder)
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "エラーが発生しました: ${e.message}", 'エラー', JOptionPane.ERROR_MESSAGE)
        }
    }

    private void displayResults(ArrayList<SearchResult> results, String rootFolder) {
        resultCountLabel.setText("検索結果件数: ${results.size()}")
        
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel()
        model.setRowCount(0)  // 既存データをクリア
        
        results.each { result ->
            String relativePath = new File(rootFolder).toPath()
                .relativize(new File(result.filePath).toPath()).toString()
            model.addRow([
                relativePath,
                result.lineNo,
                result.columnNo,
                result.line
            ] as Object[])
        }
        
        // 列幅を調整
        resultTable.columnModel.getColumn(0).preferredWidth = 300  // ファイルパス
        resultTable.columnModel.getColumn(1).preferredWidth = 80   // 行番号
        resultTable.columnModel.getColumn(2).preferredWidth = 80   // 列番号
        resultTable.columnModel.getColumn(3).preferredWidth = 400  // 行内容
    }

    private void handleTableDoubleClick() {
        boolean failedCommand = false
        try {
            int selectedRow = resultTable.getSelectedRow()
            if (selectedRow < 0) return
            
            DefaultTableModel model = (DefaultTableModel) resultTable.getModel()
            String relativePath = model.getValueAt(selectedRow, 0) as String
            int lineNo = model.getValueAt(selectedRow, 1) as int
            
            String absolutePath = new File(rootFolderField.getText(), relativePath).getAbsolutePath()
            String command = configManager.interpolateCommand(absolutePath, lineNo)
            
            // コマンド実行
            def process = command.execute()
            int exitCode = process.waitFor()
            if (exitCode != 0) {
                LOG.error("コマンド実行失敗: コマンド=${command}, 終了コード=${exitCode} エラー出力=${process.err.text}")
                failedCommand = true
            }
        } catch (Exception e) {
            LOG.error("コマンド実行エラー: コマンド=${command}", e)
            failedCommand = true
        }
        if (failedCommand) {
            JOptionPane.showMessageDialog(this, "コマンドの実行に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE)
        }
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater({
            new App().setVisible(true)
        })
    }
}
