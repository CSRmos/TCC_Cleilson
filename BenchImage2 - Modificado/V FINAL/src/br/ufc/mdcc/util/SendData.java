package br.ufc.mdcc.util;

import java.text.DateFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import br.ufc.mdcc.benchimage2.dao.ResultDao;
import br.ufc.mdcc.benchimage2.dao.model.ResultImage;
import com.google.gson.Gson;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public final class SendData extends AsyncTask<Void, Void, Void> {
	private final String clsName = SendData.class.getName();

	private final String pattern = "dd-MM-yyyy HH:mm:ss";
	private final DateFormat dateFormat = new SimpleDateFormat(pattern,
			Locale.US);

	private SocketTask st;

	private Context context;
	private ProgressDialog progressDialog;

	private ResultDao dao;

	private String num_sorteio;

	public SendData(Context context, String num_sorteio) {
		this.context = context;
		this.num_sorteio = num_sorteio;
		dao = new ResultDao(context);
	}

	@Override
	protected void onPreExecute() {

		progressDialog = ProgressDialog.show(context, "",
				"Enviando Dados Para o Servidor...", true);
		progressDialog.show();

		st = new SocketTask("192.168.100.10", 7896, 5000) {
			@Override
			protected void onProgressUpdate(String... progress) {
			}
		};
	}

	protected Void doInBackground(Void... params) {
		try {
			int count = 0;
			List<Informations> dados = new ArrayList<Informations>();
			Informations infor = null;
			ListofInformations lista = new ListofInformations();

			ArrayList<ResultImage> results = dao.getAll();

			for (ResultImage result : results) {
				infor = new Informations();
				infor.setCount(++count + "");
				infor.setDate(dateFormat.format(result.getDate()));
				infor.setDsk(num_sorteio);
				infor.setLocal(result.getConfig().getLocal());
				infor.setSize(result.getConfig().getSize());
				infor.setCpuTime(result.getRpcProfile().getExecutionCpuTime()+ "");
				infor.setUp_Time(result.getRpcProfile().getUploadTime() + "");
				infor.setDown_Time(result.getRpcProfile().getDonwloadTime()+ "");
				infor.setTotal_Time(result.getTotalTime() + "");
				infor.setUp_Size(result.getRpcProfile().getUploadSize() + "");
				infor.setDown_Size(result.getRpcProfile().getDownloadSize()+ "");
				
				infor.setID(Build.ID);
				infor.setMem(Build.MANUFACTURER);
				infor.setModel(Build.MODEL);

				dados.add(infor);
			}
			
			lista.setLista(dados);
			String json = new Gson().toJson(lista);
			st.execute(json);
			dao.clean();

		} catch (JSONException e) {
			Log.w(clsName, e);
		} catch (ParseException e) {
			Log.w(clsName, e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		Toast.makeText(context, "Envio de Dados Finalizado!", Toast.LENGTH_LONG).show();
	}

}