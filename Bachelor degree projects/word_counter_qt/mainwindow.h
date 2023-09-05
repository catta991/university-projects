#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtCharts>
#include <QBarSet>
#include <QBarSeries>
#include <QChartView>



QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);

    ~MainWindow();



private slots:

    void on_loadFile_clicked();

    void on_csvFile_clicked();

    void on_graphButton_clicked();

    void on_chiudi_clicked();

private:
    Ui::MainWindow *ui;
    std::string filename;
    std::string fileNameWithPath;
    std::string data;
    unsigned int wordNumber;
    unsigned int paragraphNumber;
    unsigned int characterWithSpace;
    unsigned int characterNoSpace;
    unsigned int phraseNumber;
    QBarSet *set0;
    QBarSet *set1;
    QBarSet *set2;
    QBarSet *set3;
    QBarSet *set4;
    QBarSeries *series;
    QChart *chart;
    QValueAxis *axisY;
     QChartView *chartView;
    void updateUI();
    void readFile();

};
#endif // MAINWINDOW_H
