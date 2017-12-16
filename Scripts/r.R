#!/usr/bin/R

library("Hmisc")
args<-commandArgs(TRUE)

#define a quantidade de argumentos
if (length(args) < 4){
    stop("Error! Insert the args!!!")
}

#passa os argumentos para as variaveis
a<-read.table(args[1])[,1]
b<-read.table(args[2])[,1]
d<-read.table(args[3])[,1]
e<-read.table(args[4])[,1]

#calcula as medias dos arquivos recebido
heights = c(mean(a)/1000,mean(b)/1000,mean(d)/1000,mean(e)/1000)

#seta os valores para X
x<-c(1,2,3,4)

#seta os valores dos intervalos de confiança
dinf<-heights-c(1.96*sd(a)/sqrt(length(a))/1000, 1.96*sd(b)/sqrt(length(b))/1000, 1.96*sd(d)/sqrt(length(d))/1000, 1.96*sd(e)/sqrt(length(e))/1000)
dsup<-heights+c(1.96*sd(a)/sqrt(length(a))/1000, 1.96*sd(b)/sqrt(length(b))/1000, 1.96*sd(d)/sqrt(length(d))/1000, 1.96*sd(e)/sqrt(length(e))/1000)

#passa os parametros para a criação dos graficos
# png("rtts.png")
bp <-barplot(las=2, heights, ylim=c(0,50), col=c("red" , "skyblue", "yellow", "green", "blue", "skyblue"), legend.text = c("TEMPO DE CPU", "TEMPO DE UPLOAD", "TEMPO DE DOWNLOAD", "TEMPO TOTAL"), args.legend = list(x = "top"))

errbar(bp[,1], heights, dsup, dinf, add=T)
# dev.off()


