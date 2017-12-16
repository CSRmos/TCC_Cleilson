/*******************************************************************************
 * Copyright (C) 2014 Philipp B. Costa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package br.ufc.mdcc.benchimage2;

import java.io.File;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import br.ufc.mdcc.benchimage2.R;
import br.ufc.mdcc.benchimage2.dao.ResultDao;
import br.ufc.mdcc.benchimage2.dao.model.AppConfiguration;
import br.ufc.mdcc.benchimage2.dao.model.ResultImage;
import br.ufc.mdcc.benchimage2.image.CloudletFilter;
import br.ufc.mdcc.benchimage2.image.Filter;
import br.ufc.mdcc.benchimage2.image.ImageFilter;
import br.ufc.mdcc.benchimage2.image.ImageFilterTask;
import br.ufc.mdcc.benchimage2.image.InternetFilter;
import br.ufc.mdcc.mpos.MposFramework;
import br.ufc.mdcc.mpos.config.Inject;
import br.ufc.mdcc.mpos.config.MposConfig;
import br.ufc.mdcc.mpos.util.TaskResultAdapter;
import br.ufc.mdcc.util.SendData;

/**
 * @author Philipp
 */

@MposConfig(endpointSecondary = "52.67.51.13")
public final class MainActivity extends Activity {
	private final String clsName = MainActivity.class.getName();
	
	private ResultDao dao;

	private Filter filterLocal = new ImageFilter();

	@Inject(ImageFilter.class)
	private CloudletFilter cloudletFilter;

	@Inject(ImageFilter.class)
	private InternetFilter internetFilter;

	private AppConfiguration config;

	private boolean quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		quit = false;
		MposFramework.getInstance().start(this);

		config = new AppConfiguration();

		configureButtonExecuteLocal();
		buttonExecuteStatusChange(R.id.button_execute_cloudlet, true, "Iniciar Local");
		configureButtonExecuteCloudlet();
		buttonExecuteStatusChange(R.id.button_execute_cloudlet, false, "Aguarde");
		configureButtonExecuteNuvem();
		buttonExecuteStatusChange(R.id.button_execute_nuvem, false, "Aguarde");
		
		createDirOutput();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (quit) {
			MposFramework.getInstance().stop();
			Process.killProcess(Process.myPid());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		switch (item.getItemId()) {
		case R.id.menu_action_export:
			alertDialogBuilder.setTitle("Enviar Resultados");
			alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			alertDialogBuilder.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new SendData(MainActivity.this, "0000").execute();
							
							buttonExecuteStatusChange(R.id.button_execute_local, true,"Iniciar Local");
							buttonExecuteStatusChange(R.id.button_execute_cloudlet, false,"Aguarde");
							buttonExecuteStatusChange(R.id.button_execute_nuvem, false,"Aguarde");
						}
					});
			alertDialogBuilder.setNegativeButton(R.string.button_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alertDialogBuilder.setMessage("Deseja Enviar os Resultados?");
			alertDialogBuilder.create().show();
			break;
		}
		return true;
	}

	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.alert_exit_title);
		alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialogBuilder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						quit = true;
						finish();
					}
				});
		alertDialogBuilder.setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialogBuilder.setMessage(R.string.alert_exit_message);
		alertDialogBuilder.create().show();
	}

	private void processLocal() {
		dao = new ResultDao(getApplication());
		dao.clean();
		
		configureAplicationLocal();
		new ImageFilterTask(getApplication(), filterLocal, config,taskResultAdapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private void processCloudlet() {
		configureAplicationCloudlet();
		new ImageFilterTask(getApplication(), cloudletFilter, config,taskResultAdapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private void processNuvem() {
		configureAplicationNuvem();
		new ImageFilterTask(getApplication(), internetFilter, config,taskResultAdapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

	private void configureButtonExecuteLocal() {
		Button but = (Button) findViewById(R.id.button_execute_local);
		but.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				configureStatusView("Executando Processamento Local");
				
				buttonExecuteStatusChange(R.id.button_execute_local, false,"Processando");
				buttonExecuteStatusChange(R.id.button_execute_cloudlet, false,"Aguarde");
				buttonExecuteStatusChange(R.id.button_execute_nuvem, false,"Aguarde");
				processLocal();	
			}
		});
	}

	private void configureButtonExecuteCloudlet() {
		Button but1 = (Button) findViewById(R.id.button_execute_cloudlet);
		but1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				configureStatusView("Executando Processamento na Cloudlet");
				
				buttonExecuteStatusChange(R.id.button_execute_local, false,"Aguarde");
				buttonExecuteStatusChange(R.id.button_execute_cloudlet, false,"Processando");
				buttonExecuteStatusChange(R.id.button_execute_nuvem, false,"Aguarde");
				processCloudlet();
				
			}
		});
	}
	
	private void configureButtonExecuteNuvem() {
		Button but2 = (Button) findViewById(R.id.button_execute_nuvem);
		but2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				configureStatusView("Executando Processamento na Nuvem");
				
				buttonExecuteStatusChange(R.id.button_execute_local, false,"Aguarde");
				buttonExecuteStatusChange(R.id.button_execute_cloudlet, false,"Aguarde");
				buttonExecuteStatusChange(R.id.button_execute_nuvem, false,"Processando");
				processNuvem();
				
			}
		});
	}
	
	private void configureStatusView(String status) {
		TextView tv_status = (TextView) findViewById(R.id.text_status);
		tv_status.setText(status);
	}

	private void configureAplicationCloudlet() {
		config.setImage("img4.jpg");
		config.setFilter("Cartoonizer");
		config.setSize("1MP");
		config.setLocal("Cloudlet");
	}

	private void configureAplicationNuvem() {
		config.setImage("img4.jpg");
		config.setFilter("Cartoonizer");
		config.setSize("1MP");
		config.setLocal("Internet");
	}

	private void configureAplicationLocal() {
		config.setImage("img4.jpg");
		config.setFilter("Cartoonizer");
		config.setSize("1MP");
		config.setLocal("Local");
	}

	private void buttonExecuteStatusChange(int id, boolean state, String text) {
		Button but = (Button) findViewById(id);
		but.setEnabled(state);
		but.setText(text);
	}

	private void createDirOutput() {
		File storage = Environment.getExternalStorageDirectory();
		String outputDir = storage.getAbsolutePath() + File.separator
				+ "BenchImageOutput";

		File dir = new File(outputDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		config.setOutputDirectory(outputDir);
	}

	private TaskResultAdapter<ResultImage> taskResultAdapter = new TaskResultAdapter<ResultImage>() {
		@Override
		public void completedTask(ResultImage obj) {
			if (obj == null) {
				TextView tv_status = (TextView) findViewById(R.id.text_status);
				tv_status.setText("Status: Algum Error na transmissão!");
				
				dao = new ResultDao(getApplication());
				dao.clean();
				
				buttonExecuteStatusChange(R.id.button_execute_local, true,"Iniciar Local");
				buttonExecuteStatusChange(R.id.button_execute_cloudlet, false,"Aguarde");
				buttonExecuteStatusChange(R.id.button_execute_nuvem, false,"Aguarde");
			}
			
			if ( config.getLocal() == "Local" ){
				buttonExecuteStatusChange(R.id.button_execute_local, false,"Finalizado");
				buttonExecuteStatusChange(R.id.button_execute_cloudlet, true,"Iniciar Cloudlet");
			}else if ( config.getLocal() == "Cloudlet" ){
				buttonExecuteStatusChange(R.id.button_execute_cloudlet, false,"Finalizado");
				buttonExecuteStatusChange(R.id.button_execute_nuvem, true,"Iniciar Nuvem");
			} else {
				buttonExecuteStatusChange(R.id.button_execute_nuvem, true,"Finalizado");
				configureStatusView("Processamento Finalizado!!!");
			}
		}

		@Override
		public void taskOnGoing(int completed, String statusText) {
			TextView tv_status = (TextView) findViewById(R.id.text_status);
			tv_status.setText("Status: " + statusText);
		}
	};
}