# springboot-managesys

## はじめに
本プロジェクトは、[Spring Boot](http://spring.io/projects/spring-boot)と[Hibernate](http://hibernate.org/)を利用した書籍管理システムのプロジェクトである。
本プロジェクトではアプリケーションの機能をWeb APIとして提供するため、画面は[vuejs-managesys](https://github.com/ybkuroki/vuejs-managesys)を利用する。

## 開発環境構築手順
以下の手順で開発環境を構築する。

1. [MergeDoc Project](http://mergedoc.osdn.jp/)より、Eclipse Pleiades All in One(Gradle PluginがプリインストールされているMars以降を推奨)をダウンロード・インストールする。

1. メニューバーより、「ファイル」→「インポート」を押下する。

1. 「Gradle」→「既存のGradleプロジェクト」を選択し、「次へ」ボタンを押下する。

1. 「プロジェクト・ルート・ディレクトリ」にてプロジェクトフォルダを選択する。「完了」ボタンを押下する。

1. しばらく待つとプロジェクトのインポートが完了する。

## 動作確認手順
以下の手順で動作を確認する。

1. プロジェクト直下より、「src/main/java」→「managesys」→「Application.java」を選択する。

1. 右クリックし、「実行」→「Javaアプリケーション」を選択する。

1. コンソールに「Started Application」と表示されたら、起動完了。

1. [http://localhost:8080/api/health](http://localhost:8080/api/health)にアクセスし、下記JSONコードが表示されることを確認する。

    ```
    {"status":"UP","details":{"diskSpace":{"status":"UP","details":{"total":253796282368,"free":202889191424,"threshold":10485760}},"db":{"status":"UP","details":{"database":"H2","hello":1}}}}
    ```

## テスト実行手順
Gradleタスクタブより「verification」→「test」を選択し実行する。

> Gradleタスクタブが表示されてない場合は、「ウインドウ」→「ビューの表示」→「その他」を押下する。そして、「Gradle」より、「Gradleタスク」を選択して表示する。

## 配布用ビルド手順
Gradleタスクタブより「build」→「bootJar」を押下する。

## プロジェクト構成
プロジェクト構成は以下の通りである。

```
- src/main/java
  + common                  … システムの共通機能を提供
  + configration            … システムの構成設定を記述
  + controller              … Restコントローラを表現
  + model                   … ドメインモデルを表現
  + report                  … 帳票出力機能を提供
  + repository              … データベースアクセス機能を提供
  + security                … 認証機能を提供
  + service                 … 書籍管理機能を提供
  - Application.java        … アプリケーションのエントリポイント

- src/main/resources        … 環境ごとの構成設定を記述

- src/test/java
  + configration            … テスト実行用の構成設定を記述
  + managesys               … 単体テストを記述
```

## 機能一覧
本システムが提供する機能は、書籍管理機能、アカウント管理機能、マスタ管理機能の3つである。

また、本システムはSwaggerを利用して下記Web APIを[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)から確認できる。

### 書籍管理機能
以下の機能を書籍管理機能として提供する。

|機能名|HTTP方式|URL|パラメータ|概要|
|:---|:---:|:---|:---|:---|
|一覧取得機能|GET|``/api/book/list``|ページング情報|書籍一覧を取得する。|
|登録機能|POST|``/api/book/new``|書籍情報|書籍データを新規登録する。|
|編集機能|POST|``/api/book/edit``|書籍情報|既存の書籍データを編集する。|
|削除機能|POST|``/api/book/delete``|書籍情報|既存の書籍データを削除する。|
|タイトル検索|GET|``/api/book/search``|キーワード、ページング情報|指定したキーワードでタイトルを曖昧検索する。|
|帳票出力機能|GET|``/api/book/allListPdfReport``|なし|書籍一覧をPDF形式で出力する。|

### アカウント管理機能
以下の機能をアカウント管理機能として提供する。

|機能名|HTTP方式|URL|パラメータ|概要|
|:---|:---:|:---|:---|:---|
|ログイン機能|POST|``/api/account/login``|セッションID、ユーザ名、パスワード|ユーザ名とパスワードによるセッション認証を提供する。|
|ログアウト機能|POST|``/api/account/logout``|セッションID|ログインユーザをログアウトする機能を提供する。|
|ログイン状態確認機能|GET|``/api/account/loginStatus``|セッションID|ユーザがログイン済みかどうかを確認する。|
|ログインユーザ名取得機能|GET|``/api/account/loginAccount``|セッションID|ログインユーザのユーザ名を取得する。|

### マスタ管理機能
以下の機能をマスタ管理機能として提供する。

|機能名|HTTP方式|URL|パラメータ|概要|
|:---|:---:|:---|:---|:---|
|カテゴリ一覧取得機能|GET|``/api/master/category``|なし|カテゴリ一覧を取得する。|
|書籍フォーマット一覧取得機能|GET|``/api/master/format``|なし|書籍フォーマット一覧を取得する。|

## 主要利用ライブラリ
以下のライブラリを主に利用している。

|ライブラリ名|バージョン|概要|
|:---|:---:|:---|
|spring-boot-starter-web|2.2.0|MVC概念を提供する。|
|spring-boot-starter-security|2.2.0|ログイン認証機能とセキュリティ対策機能を提供する。|
|spring-boot-starter-data-jpa|2.2.0|Hibernateを利用したデータベースアクセス機能を提供する。|
|spring-boot-starter-actuator|2.2.0|システムの状態確認機能を提供する。|
|spring-boot-starter-logging|2.2.0|ロギング機能を提供する。|
|spring-boot-starter-aop|2.2.0|アスペクト指向の基底概念を提供する。|
|spring-boot-starter-test|2.2.0|単体テストのための基底実装を提供する。|
|pdfbox|2.0.14|PDF出力機能を提供する。|
|springfox-swagger2|2.7.0|SwaggerによるWeb APIのドキュメント生成機能を提供する。|
|h2|1.4.+|メモリベースデータベース機能を提供する。|

## ライセンス
本プロジェクトのライセンスは、MITライセンスです。

