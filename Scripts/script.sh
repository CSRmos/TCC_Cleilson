#!/bin/bash

qtd_user=$1
dir=$2

arquivos=$(ls $dir | cut -d" " -f1 | wc -l)

for ((z=1;z<=$((arquivos));z++))
do
	arquivo=$(ls $dir | cut -d" " -f1 | head -n $z | tail -n 1)
	echo "########################## ARQUIVO" $arquivo "#############################"

	sizes=( "4" "2" "1" )
	locais=( "LOCAL" "CLOUDLET" "NUVEM" )
	cont=1

	for ((k=0;k<$qtd_user;k++))
	do
		for ((j=0;j<3;j++))
		do
			for ((i=0;i<3;i++))
			do
				cont=$(($cont+1))

				cpu_time=$(cat /home/cleilson/$dir$arquivo | sed 's/,/;/g' | head -n $cont | tail -n 1 | cut -d";" -f7)
				up_time=$(cat /home/cleilson/$dir$arquivo | sed 's/,/;/g' | head -n $cont | tail -n 1 | cut -d";" -f8)
				down_time=$(cat /home/cleilson/$dir$arquivo | sed 's/,/;/g' | head -n $cont | tail -n 1 | cut -d";" -f9)
				total_time=$(cat /home/cleilson/$dir$arquivo | sed 's/,/;/g' | head -n $cont | tail -n 1 | cut -d";" -f10)

				echo $cpu_time >> /home/cleilson/RESULT.TESTES/$qtd_user\_USER/${sizes[$j]}\MP/${locais[$i]}/CPU_TIME
				echo $up_time >> /home/cleilson/RESULT.TESTES/$qtd_user\_USER/${sizes[$j]}\MP/${locais[$i]}/UP_TIME
				echo $down_time >> /home/cleilson/RESULT.TESTES/$qtd_user\_USER/${sizes[$j]}\MP/${locais[$i]}/DOWN_TIME
				echo $total_time >> /home/cleilson/RESULT.TESTES/$qtd_user\_USER/${sizes[$j]}\MP/${locais[$i]}/TOTAL_TIME
			done
		done
	done
done
