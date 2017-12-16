package br.ufc.mdcc.util;

import java.lang.String;

public class Informations {
	String ID;
	String Date;
	String Mem;
	String Dsk;
	String Model;
	String Local;
	String Size;
	String CpuTime;
	String Up_Time;
	String Down_Time;
	String Total_Time;
	String Up_Size;
	String Down_Size;
	String Count;

	public Informations() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Informations(String iD, String date, String mem, String dsk,
			String model, String local, String size, String cpuTime,
			String up_Time, String down_Time, String total_Time,
			String up_Size, String down_Size, String count) {
		super();
		ID = iD;
		Date = date;
		Mem = mem;
		Dsk = dsk;
		Model = model;
		Local = local;
		Size = size;
		CpuTime = cpuTime;
		Up_Time = up_Time;
		Down_Time = down_Time;
		Total_Time = total_Time;
		Up_Size = up_Size;
		Down_Size = down_Size;
		Count = count;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getMem() {
		return Mem;
	}

	public void setMem(String mem) {
		Mem = mem;
	}

	public String getDsk() {
		return Dsk;
	}

	public void setDsk(String dsk) {
		Dsk = dsk;
	}

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getLocal() {
		return Local;
	}

	public void setLocal(String local) {
		Local = local;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getCpuTime() {
		return CpuTime;
	}

	public void setCpuTime(String cpuTime) {
		CpuTime = cpuTime;
	}

	public String getUp_Time() {
		return Up_Time;
	}

	public void setUp_Time(String up_Time) {
		Up_Time = up_Time;
	}

	public String getDown_Time() {
		return Down_Time;
	}

	public void setDown_Time(String down_Time) {
		Down_Time = down_Time;
	}

	public String getTotal_Time() {
		return Total_Time;
	}

	public void setTotal_Time(String total_Time) {
		Total_Time = total_Time;
	}

	public String getUp_Size() {
		return Up_Size;
	}

	public void setUp_Size(String up_Size) {
		Up_Size = up_Size;
	}

	public String getDown_Size() {
		return Down_Size;
	}

	public void setDown_Size(String down_Size) {
		Down_Size = down_Size;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	@Override
	public String toString() {
		return "Informations [ID=" + ID + ", Date=" + Date + ", Mem=" + Mem
				+ ", Dsk=" + Dsk + ", Model=" + Model + ", Local=" + Local
				+ ", Size=" + Size + ", CpuTime=" + CpuTime + ", Up_Time="
				+ Up_Time + ", Down_Time=" + Down_Time + ", Total_Time="
				+ Total_Time + ", Up_Size=" + Up_Size + ", Down_Size="
				+ Down_Size + ", Count=" + Count + "]";
	}
}