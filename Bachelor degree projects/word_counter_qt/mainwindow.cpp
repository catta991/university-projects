#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QFile>
#include <QTextStream>
#include <QCoreApplication>
#include <QFileDialog>
#include <iostream>
#include <QMessageBox>
#include <QChar>


// N.B i risultati sono stati confrontati con i risultati ottenuti da microsoft word


// costruttore della main window dove assegno un valore di default alle stringhe e ai puntatore
//e assegno alla main window una dimensione fissa
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setFixedSize(980, 540);
    std::string filename ="";
    std::string fileNameWithPath="";
    std::string data="";
    set0=nullptr;
    set1=nullptr;
    set2=nullptr;
    set3=nullptr;
    set4=nullptr;
    chart=nullptr;
    series=nullptr;
    chartView=nullptr;
    axisY=nullptr;


}


// distruttore dove vado a eliminare tutti i puntatori al grafico e il puntatore all'ui
MainWindow::~MainWindow()
{

    delete set0;
    delete set1;
    delete set2;
    delete set3;
    delete set4;
    delete series;
    delete chart;
    delete chartView;

    // axisY essendo figlio di chart o di chartView
    // viene cancellato quando viene cancellato il padre
    delete ui;
}

// metodo che aggiorna la ui richiamando il metodo per leggere il file, rendendo enable i bottoni e
// contando i vari elementi che poi verranno inseriti nella UI
void MainWindow::updateUI(){

     readFile();

     // setto il testo del file nella edit text e rendo visibili i bottoni per creare
     // il file csv e visualizzare il grafico

    QString Qdata = QString::fromStdString(this->data);

    ui -> fileText->setText(Qdata);
    ui -> fileName -> setText(QString::fromStdString(this->filename));
    ui->csvFile->setEnabled(true);
    ui->graphButton->setEnabled(true);



    /** conta caratteri con spazi **/

    QString yesSpaces = Qdata;
    yesSpaces = yesSpaces.trimmed(); // toglie il \0 alla fine
    yesSpaces.remove(QChar('\n'), Qt::CaseInsensitive); // toglie il /n dalla stringa
    yesSpaces.remove(QChar('\a'), Qt::CaseInsensitive); // toglie il /a dalla stringa
    yesSpaces.remove(QChar('\b'), Qt::CaseInsensitive); // toglie il /b dalla stringa
    yesSpaces.remove(QChar('\f'), Qt::CaseInsensitive); // toglie il /f dalla stringa
    yesSpaces.remove(QChar('\v'), Qt::CaseInsensitive); // toglie il /v dalla stringa
    yesSpaces.remove(QChar('\r'), Qt::CaseInsensitive); // toglie il /r dalla stringa
    
    ui -> numeroCarInc -> setText(QString::number(yesSpaces.length()));

    characterWithSpace = yesSpaces.length();

   /** fine conta caratteri con spazi **/


  /** conta caratteri senza spazi **/

    QString noSpaces = yesSpaces;
    unsigned int voidSpaces = noSpaces.count(' '); // conto gli spazi vuoti
    unsigned int tabSpaces = noSpaces.count('\t'); // conto gli spazi di tabulazione
    ui->numeroCarEsc -> setText(QString::number(noSpaces.length()-voidSpaces-tabSpaces)); // prendo la lunghezza della stringa e sottraggo il numero di spazi vuoti e di tabulazione

    characterNoSpace = noSpaces.length()-voidSpaces-tabSpaces;

  /** fine conta caratteri senza spazi **/

  /** inizio conteggio parole frasi e paragrafi **/

    unsigned int spaceNumber = Qdata.count(' '); // conto i caratteri vuoti
    unsigned int tabCounter = Qdata.count('\t'); // conto i caratteri di tabulazione
    unsigned int endLineCounter = Qdata.count('\n'); // conto i caratteri di new line
    unsigned int dotCounter = Qdata.count('.'); // conto i punti
    unsigned int questionMarkCounter = Qdata.count('?'); // conto i punti interrogativi
    unsigned int exclamationPointCounter = Qdata.count('!'); // conto i punti esclamativi

    QChar curr;
    QChar prev;
// ciclo che serve per non conteggaire doppi spazi vuoti e paragrafi vuoti e la 
// punteggiatura ripetuta

    for(int i=0; i< Qdata.size(); i++){
        if(i==0){
            curr = Qdata[0];
        }else{

            prev = curr;
            curr = Qdata[i];

            if((prev == ' ' && curr == ' ') || (curr=='\n' && prev == ' ') || (curr=='\n' && prev == '\t'))
                -- spaceNumber;

            if((prev == '\t' && curr == '\t') || (prev == '\t' && curr == ' ') || (prev == ' ' && curr == '\t'))
                -- spaceNumber;

            if(prev == '\n' && curr == '\n')
                -- endLineCounter;
        

            // controllo punteggiatura ripetuta

            if(prev == '!' && curr == '!')
                -- exclamationPointCounter;

            if(prev == '?' && curr == '?')
                -- questionMarkCounter;

            if(prev == '.' && curr == '.')
                -- dotCounter;

             if(i==Qdata.size()-1 && curr != '\n')
                ++ endLineCounter;


        }
    }



    unsigned int WordNumber =spaceNumber + tabCounter+ endLineCounter;
    unsigned int phrases=  dotCounter + questionMarkCounter + exclamationPointCounter;
    unsigned int paragraph=endLineCounter;


    ui -> nPar -> setText(QString::number(WordNumber));

    ui -> nPar_2 ->setText(QString::number(paragraph));

    ui ->nFr -> setText(QString::number(phrases));

    wordNumber =WordNumber;
    paragraphNumber = paragraph;
    phraseNumber = phrases;

  /** fine conteggio parole frasi e paragrafi **/

}


// metodo che permette di leggere il contenuto del file desiderato avendo il nome del file
void MainWindow::readFile(){

 QString fl = QString::fromStdString(fileNameWithPath);

    QFile file(fl);

   if(!file.open(QIODevice::ReadOnly)){

       std::cerr << "Impossibile leggere il file: "
       << qPrintable(file.errorString())
       << std::endl;
       return;
   }

   QTextStream stream(&file);



    QString dt = stream.readAll();

       data = dt.toStdString();

   file.close();




}



// metodo che mediante una QFileDialog permette di selezionare un file di testo dal file system
// partendo dalla cartella home del pc
void MainWindow::on_loadFile_clicked()
{



     QString fPath =QFileDialog::getOpenFileName(  // nome del file con indirizzo
                    this,
                    tr("open file"),
                    QDir::homePath(),
                    tr("Text File (*.txt)"));




// se è stato selezionato un file mi permette di ottenere solo il nome del file e una volta fatto richiama il metodo update UI

    if(fPath.length()>0){

    fileNameWithPath = fPath.toStdString();

    unsigned int lastOcc = fPath.lastIndexOf('/');

    QString fileName = fPath.right(fPath.length()-lastOcc-1);

    fileName = fileName.left(fileName.length()-4);

    filename = fileName.toStdString();

    updateUI();
}


}


// metodo che permette di creare, un file csv,
//con lo stesso nome del documento aperto, contenete le varie informazioni del testo
// il file viene salvato nella stessa cartella del progetto
void MainWindow::on_csvFile_clicked()
{
    QString fileName= QString::fromStdString(filename);

    QFile file(fileName+".csv");

    if(!file.open(QFile::WriteOnly | QFile::Truncate) )
    {
        std::cerr << "Impossibile aprire il file in scrittura: "
        << qPrintable(file.errorString())
        << std::endl;
        return;
    }

    QTextStream out(&file);

    out << "il numero di caratteri (punteggiatura e spazi inclusi) e'," << characterWithSpace << "\n";
    out << "il numero di caratteri (spazi esclusi) e'," << characterNoSpace << "\n";
    out << "il numero di frasi e'," << phraseNumber << "\n";
    out << "il numero di parole e'," << wordNumber << "\n";
    out << "il numero di paragrafi e'," << paragraphNumber << "\n";

   // box che stampa un avviso di corretta creazione del file csv

    QMessageBox::information(this, tr("avviso"), "il file csv è stato creato correttamente");

    out.flush();
    file.close();

}

//metodo che permette di creare un grafico a barre che contiene
// le varie informazioni del testo
void MainWindow::on_graphButton_clicked()
{

    // setting dei vari elementi per creare un grafico a barre

       if(set0!=nullptr)
           delete set0;

      set0 = new QBarSet("numero caratteri(spazi inclusi)");

      if(set1 != nullptr)
        delete set1;
      set1 = new QBarSet("numero caratteri spazi esclusi");

      if(set2 != nullptr)
        delete set2;
      set2 = new QBarSet("numero parole");

      if(set3!=nullptr)
        delete set3;
      set3 = new QBarSet("numero frasi");

      if(set4!=nullptr)
          delete set4;

      set4 = new QBarSet("numero paragrafi");


       *set0 << this-> characterWithSpace;
       *set1 << this-> characterNoSpace;
       *set2 << this ->wordNumber;
       *set3 << this ->phraseNumber;
       *set4 << this-> paragraphNumber;



    if(series != nullptr)
        delete series;

           series = new QBarSeries();
           series->append(set0);
           series->append(set1);
           series->append(set2);
           series->append(set3);
           series->append(set4);


            if(chart != nullptr)
                delete chart;

           chart = new QChart();
               chart->addSeries(series);
               chart->setTitle("grafico informazioni di: "+ QString::fromStdString(filename));
               chart->setAnimationOptions(QChart::SeriesAnimations);



             axisY = new QValueAxis();
            axisY->setRange(0,characterWithSpace);
            chart->addAxis(axisY, Qt::AlignLeft);
            series->attachAxis(axisY);


            if(chartView != nullptr)
                delete chartView;

            chartView = new QChartView(chart);
             chartView->setRenderHint(QPainter::Antialiasing);
             chartView->setFixedSize(900,550);
             chartView->show();




}



// metodo che permette di chiudere la mainWindow e chiude anche la finestra
// che contiene il grafico (se ancora aperta)
void MainWindow::on_chiudi_clicked()
{
    if(chartView != nullptr)
        chartView->close();

    close();
}

