/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.12.11
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QLabel>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QSpacerItem>
#include <QtWidgets/QTextEdit>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QWidget *verticalLayoutWidget;
    QVBoxLayout *mainLayout;
    QHBoxLayout *horizontalLayoutLoadFile;
    QPushButton *loadFile;
    QLabel *fileName;
    QSpacerItem *horizontalSpacer_6;
    QHBoxLayout *textLayout;
    QTextEdit *fileText;
    QVBoxLayout *infoLayout;
    QHBoxLayout *spaziEPunteggiaturaInclusa;
    QLabel *caratteriSpaziEPunteggiaturaInclusi;
    QLabel *numeroCarInc;
    QSpacerItem *horizontalSpacer;
    QHBoxLayout *spaziEPunteggiaturaEsclusa;
    QLabel *caratteriSpaziEsclusi;
    QLabel *numeroCarEsc;
    QSpacerItem *horizontalSpacer_2;
    QHBoxLayout *horizontalLayoutNFrasi;
    QLabel *numeroFrasi;
    QLabel *nFr;
    QSpacerItem *horizontalSpacer_3;
    QHBoxLayout *horizontalLayoutNParole;
    QLabel *numeroParole;
    QLabel *nPar;
    QSpacerItem *horizontalSpacer_4;
    QHBoxLayout *horizontalLayoutNparagrafi;
    QLabel *numeroParagrafi;
    QLabel *nPar_2;
    QSpacerItem *horizontalSpacer_5;
    QHBoxLayout *buttonLayout;
    QPushButton *csvFile;
    QPushButton *graphButton;
    QPushButton *chiudi;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->setEnabled(true);
        MainWindow->resize(952, 540);
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        verticalLayoutWidget = new QWidget(centralwidget);
        verticalLayoutWidget->setObjectName(QString::fromUtf8("verticalLayoutWidget"));
        verticalLayoutWidget->setGeometry(QRect(40, 30, 881, 489));
        mainLayout = new QVBoxLayout(verticalLayoutWidget);
        mainLayout->setObjectName(QString::fromUtf8("mainLayout"));
        mainLayout->setContentsMargins(0, 0, 0, 0);
        horizontalLayoutLoadFile = new QHBoxLayout();
        horizontalLayoutLoadFile->setObjectName(QString::fromUtf8("horizontalLayoutLoadFile"));
        loadFile = new QPushButton(verticalLayoutWidget);
        loadFile->setObjectName(QString::fromUtf8("loadFile"));
        QSizePolicy sizePolicy(QSizePolicy::Minimum, QSizePolicy::Fixed);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(loadFile->sizePolicy().hasHeightForWidth());
        loadFile->setSizePolicy(sizePolicy);

        horizontalLayoutLoadFile->addWidget(loadFile);

        fileName = new QLabel(verticalLayoutWidget);
        fileName->setObjectName(QString::fromUtf8("fileName"));

        horizontalLayoutLoadFile->addWidget(fileName);

        horizontalSpacer_6 = new QSpacerItem(200, 20, QSizePolicy::Fixed, QSizePolicy::Minimum);

        horizontalLayoutLoadFile->addItem(horizontalSpacer_6);


        mainLayout->addLayout(horizontalLayoutLoadFile);

        textLayout = new QHBoxLayout();
        textLayout->setObjectName(QString::fromUtf8("textLayout"));
        fileText = new QTextEdit(verticalLayoutWidget);
        fileText->setObjectName(QString::fromUtf8("fileText"));
        fileText->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
        fileText->setReadOnly(true);

        textLayout->addWidget(fileText);


        mainLayout->addLayout(textLayout);

        infoLayout = new QVBoxLayout();
        infoLayout->setObjectName(QString::fromUtf8("infoLayout"));
        spaziEPunteggiaturaInclusa = new QHBoxLayout();
        spaziEPunteggiaturaInclusa->setObjectName(QString::fromUtf8("spaziEPunteggiaturaInclusa"));
        caratteriSpaziEPunteggiaturaInclusi = new QLabel(verticalLayoutWidget);
        caratteriSpaziEPunteggiaturaInclusi->setObjectName(QString::fromUtf8("caratteriSpaziEPunteggiaturaInclusi"));

        spaziEPunteggiaturaInclusa->addWidget(caratteriSpaziEPunteggiaturaInclusi);

        numeroCarInc = new QLabel(verticalLayoutWidget);
        numeroCarInc->setObjectName(QString::fromUtf8("numeroCarInc"));
        numeroCarInc->setAlignment(Qt::AlignCenter);

        spaziEPunteggiaturaInclusa->addWidget(numeroCarInc);

        horizontalSpacer = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Minimum);

        spaziEPunteggiaturaInclusa->addItem(horizontalSpacer);


        infoLayout->addLayout(spaziEPunteggiaturaInclusa);

        spaziEPunteggiaturaEsclusa = new QHBoxLayout();
        spaziEPunteggiaturaEsclusa->setObjectName(QString::fromUtf8("spaziEPunteggiaturaEsclusa"));
        caratteriSpaziEsclusi = new QLabel(verticalLayoutWidget);
        caratteriSpaziEsclusi->setObjectName(QString::fromUtf8("caratteriSpaziEsclusi"));

        spaziEPunteggiaturaEsclusa->addWidget(caratteriSpaziEsclusi);

        numeroCarEsc = new QLabel(verticalLayoutWidget);
        numeroCarEsc->setObjectName(QString::fromUtf8("numeroCarEsc"));
        numeroCarEsc->setAlignment(Qt::AlignCenter);

        spaziEPunteggiaturaEsclusa->addWidget(numeroCarEsc);

        horizontalSpacer_2 = new QSpacerItem(40, 20, QSizePolicy::Expanding, QSizePolicy::Minimum);

        spaziEPunteggiaturaEsclusa->addItem(horizontalSpacer_2);


        infoLayout->addLayout(spaziEPunteggiaturaEsclusa);


        mainLayout->addLayout(infoLayout);

        horizontalLayoutNFrasi = new QHBoxLayout();
        horizontalLayoutNFrasi->setObjectName(QString::fromUtf8("horizontalLayoutNFrasi"));
        numeroFrasi = new QLabel(verticalLayoutWidget);
        numeroFrasi->setObjectName(QString::fromUtf8("numeroFrasi"));

        horizontalLayoutNFrasi->addWidget(numeroFrasi);

        nFr = new QLabel(verticalLayoutWidget);
        nFr->setObjectName(QString::fromUtf8("nFr"));

        horizontalLayoutNFrasi->addWidget(nFr);

        horizontalSpacer_3 = new QSpacerItem(700, 20, QSizePolicy::Fixed, QSizePolicy::Minimum);

        horizontalLayoutNFrasi->addItem(horizontalSpacer_3);


        mainLayout->addLayout(horizontalLayoutNFrasi);

        horizontalLayoutNParole = new QHBoxLayout();
        horizontalLayoutNParole->setObjectName(QString::fromUtf8("horizontalLayoutNParole"));
        numeroParole = new QLabel(verticalLayoutWidget);
        numeroParole->setObjectName(QString::fromUtf8("numeroParole"));

        horizontalLayoutNParole->addWidget(numeroParole);

        nPar = new QLabel(verticalLayoutWidget);
        nPar->setObjectName(QString::fromUtf8("nPar"));

        horizontalLayoutNParole->addWidget(nPar);

        horizontalSpacer_4 = new QSpacerItem(700, 20, QSizePolicy::Fixed, QSizePolicy::Minimum);

        horizontalLayoutNParole->addItem(horizontalSpacer_4);


        mainLayout->addLayout(horizontalLayoutNParole);

        horizontalLayoutNparagrafi = new QHBoxLayout();
        horizontalLayoutNparagrafi->setObjectName(QString::fromUtf8("horizontalLayoutNparagrafi"));
        numeroParagrafi = new QLabel(verticalLayoutWidget);
        numeroParagrafi->setObjectName(QString::fromUtf8("numeroParagrafi"));

        horizontalLayoutNparagrafi->addWidget(numeroParagrafi);

        nPar_2 = new QLabel(verticalLayoutWidget);
        nPar_2->setObjectName(QString::fromUtf8("nPar_2"));

        horizontalLayoutNparagrafi->addWidget(nPar_2);

        horizontalSpacer_5 = new QSpacerItem(700, 20, QSizePolicy::Fixed, QSizePolicy::Minimum);

        horizontalLayoutNparagrafi->addItem(horizontalSpacer_5);


        mainLayout->addLayout(horizontalLayoutNparagrafi);

        buttonLayout = new QHBoxLayout();
        buttonLayout->setObjectName(QString::fromUtf8("buttonLayout"));
        csvFile = new QPushButton(verticalLayoutWidget);
        csvFile->setObjectName(QString::fromUtf8("csvFile"));
        csvFile->setEnabled(false);

        buttonLayout->addWidget(csvFile);

        graphButton = new QPushButton(verticalLayoutWidget);
        graphButton->setObjectName(QString::fromUtf8("graphButton"));
        graphButton->setEnabled(false);

        buttonLayout->addWidget(graphButton);

        chiudi = new QPushButton(verticalLayoutWidget);
        chiudi->setObjectName(QString::fromUtf8("chiudi"));
        chiudi->setEnabled(true);

        buttonLayout->addWidget(chiudi);


        mainLayout->addLayout(buttonLayout);

        MainWindow->setCentralWidget(centralwidget);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "MainWindow", nullptr));
        loadFile->setText(QApplication::translate("MainWindow", "loadFile", nullptr));
        fileName->setText(QString());
        caratteriSpaziEPunteggiaturaInclusi->setText(QApplication::translate("MainWindow", "caratteri (punteggiatura e spazi inclusi):", nullptr));
        numeroCarInc->setText(QApplication::translate("MainWindow", "0", nullptr));
        caratteriSpaziEsclusi->setText(QApplication::translate("MainWindow", "caratteri (spazi esclusi):", nullptr));
        numeroCarEsc->setText(QApplication::translate("MainWindow", "0", nullptr));
        numeroFrasi->setText(QApplication::translate("MainWindow", "numero frasi:", nullptr));
        nFr->setText(QApplication::translate("MainWindow", "0", nullptr));
        numeroParole->setText(QApplication::translate("MainWindow", "numero parole:", nullptr));
        nPar->setText(QApplication::translate("MainWindow", "0", nullptr));
        numeroParagrafi->setText(QApplication::translate("MainWindow", "numero paragrafi:", nullptr));
        nPar_2->setText(QApplication::translate("MainWindow", "0", nullptr));
        csvFile->setText(QApplication::translate("MainWindow", "crea un csv file", nullptr));
        graphButton->setText(QApplication::translate("MainWindow", "mostraGrafico", nullptr));
        chiudi->setText(QApplication::translate("MainWindow", "chiudi", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
