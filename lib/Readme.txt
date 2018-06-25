
JAXBUseテスト用JARファイル作成

　JAXBによるバインディングの確認のため利用する JARファイルを生成します。

　src配下にソースファイルを置かない理由は、testJavaCompileタスクによりコンパイル
がされても、続く testGroovyCompileタスクでクラスファイルが削除されるためです
（削除される原因については追求していません）。

　mkjar.batをダブルクリックしてください。
　JAXBUseクラスのテストに必要となる root.jarを生成します。

　root.xsdから、以下のパッケージ配下のクラスに該当するソースを生成し、コンパイル、
JARファイル作成を実施します。

-----
io.github.longfish801.shared.xml.root
-----

以上
