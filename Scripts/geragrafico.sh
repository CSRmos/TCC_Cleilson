#!/bin/bash

#cria vetor com as quantidades de usuários que foram utilizadas
qtd_user=( "1" "2" "4" "8" "16" "32" )
#cria vetor com os tamanhos de imagem que foram utilizadas
sizes=( "1" "2" "4" )
#cria vetor com os locais de processamento
locais=( "LOCAL" "CLOUDLET" "NUVEM" )

for ((j=0;j<3;j++))
do
	for ((i=0;i<3;i++))
	do
		for ((x=0;x<3;x++))
		do
			#Plota gráfico com os tempo de processamento de CPU, Upload, Download e Total variando os tamanho e locais
			Rscript r.R RESULT.TESTES/${qtd_user[$j]}_USER/${sizes[$i]}MP/${locais[$x]}/CPU_TIME RESULT.TESTES/${qtd_user[$j]}_USER/${sizes[$i]}MP/${locais[$x]}/UP_TIME RESULT.TESTES/${qtd_user[$j]}_USER/${sizes[$i]}MP/${locais[$x]}/DOWN_TIME RESULT.TESTES/${qtd_user[$j]}_USER/${sizes[$i]}MP/${locais[$x]}/TOTAL_TIME

			#Convert o grafico ferado em pdf e copia para um diretorio especifico para melhor organização
			convert Rplots.pdf /mnt/c/TCC/graficos/${qtd_user[$j]}_USERS/$j$i$x.png

			#Informa qual grafico está sendo gerado
			echo "GRAFICO DE" ${qtd_user[$j]} "USUARIOS DE TAMANHO " ${sizes[$j]}"\MP NA" ${locais[$x]} "CRIADO"

		done
	done
done
