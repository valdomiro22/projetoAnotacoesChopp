package br.com.santos.valdomiro.anotacoeschopp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.santos.valdomiro.anotacoeschopp.adapter.RecyclerViewAdapter;
import br.com.santos.valdomiro.anotacoeschopp.controller.DBController;
import br.com.santos.valdomiro.anotacoeschopp.model.DBTabela;
import br.com.santos.valdomiro.anotacoeschopp.model.Nota;
import br.com.santos.valdomiro.anotacoeschopp.util.AppUtil;


public class MainActivity extends AppCompatActivity {

    public static final DateTimeFormatter FORMATAR_HORAS = DateTimeFormatter.ofPattern("HH:mm:ss");
    //    public static final DateTimeFormatter FORMATAR_DATA = DateTimeFormatter.ofPattern("yy-MM-dd");
    public static final DateTimeFormatter FORMATAR_DATA_PARA_PDF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATAR_DATA_PARA_NOME = DateTimeFormatter.ofPattern("dd_MM_yyyy");

    static DBController dbController;
    @SuppressLint("StaticFieldLeak")
    static Context context;

    EditText editNumeroBarril;
    Button btnAdicionar, btnGerarPDF;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtQuantidadeDeBarris;

    Nota nota;

    List<String> numeros = new ArrayList<>();
    List<String> horarios = new ArrayList<>();

    boolean isFormularioOk;

    private static final int CREATEPDF = 1;

    // RecyclerView
    static RecyclerView recyclerView;
    static RecyclerView.LayoutManager layoutManager;
    static RecyclerViewAdapter recyclerViewAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarFormulario();

        // Criação do banco de dados
        context = MainActivity.this;
        dbController = new DBController(context);

        listarNoLogCat();
        preencherListView();

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFormularioOk = validarFormulario();

                if (isFormularioOk) {
                    nota = new Nota();
                    nota.setCodigo(editNumeroBarril.getText().toString());
                    nota.setHora(LocalDateTime.now().format(FORMATAR_HORAS).toString());
                    dbController.incluir(nota);
                    editNumeroBarril.setText("");
                    editNumeroBarril.getFocusable();
                    preencherListView();

                    Log.d(AppUtil.TAG, "onCreate: Dado inserido com sucesso");
                } else {
                    editNumeroBarril.getFocusable();
                    Toast.makeText(getApplicationContext(), "Verifique o campo", Toast.LENGTH_SHORT).show();
                }

                preencherQuantidadeDeBarris();

            }
        });

//        btnGerarPDF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                criarPdf("barris_" + LocalDateTime.now().format(FORMATAR_DATA_PARA_NOME));
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        preencherListView();

        preencherQuantidadeDeBarris();
    }


    public void inicializarFormulario() {
        editNumeroBarril = findViewById(R.id.edit_numero_barril);
        btnAdicionar = findViewById(R.id.btn_adicionar);
//        btnGerarPDF = findViewById(R.id.btn_gerar_pdf);
        txtQuantidadeDeBarris = findViewById(R.id.txt_quantidade_barris);

        // Do RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
//        gerenciadorDeLayout = new LinearLayoutManager(context);
    }

    public void inserirMain() {
        nota = new Nota();

        nota.setCodigo("392334");
        nota.setHora(LocalDateTime.now().format(FORMATAR_HORAS).toString());

        if (dbController.incluir(nota)) {
            Toast.makeText(MainActivity.this, "Dados incluidos com sucesso", Toast.LENGTH_LONG).show();
            Log.d(AppUtil.TAG, "inserirMain: Dados incluidos com sucesso");
        } else {
            Toast.makeText(MainActivity.this, "Dados não incluidos", Toast.LENGTH_LONG).show();
            Log.i(AppUtil.TAG, "inserirMain: Dados não incluidos");
        }
    }

    public void listarNoLogCat() {
//        List<Nota> notas = dbController.listarController();
//
//        for (Nota nota : notas) {
//            Log.i(AppUtil.TAG, "listarMain:" + " ID -> " + nota.getId() +
//                    " || Codigo: " + nota.getCodigo() +
//                    " || Hora: " + nota.getHora());
//        }
    }

    public static void preencherListView() {
        List<Nota> notas = dbController.listarController();

        List<String> codigos = new ArrayList<>();
        List<String> horas = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        String[] dados_codigos;
        String[] dados_horas;
        String[] dados_id;

        for (Nota nota : notas) {
            codigos.add(nota.getCodigo());
            horas.add(nota.getHora());
            ids.add(String.valueOf(nota.getId()));
        }

        dados_codigos = codigos.toArray(new String[0]);
        dados_horas = horas.toArray(new String[0]);
        dados_id = ids.toArray(new String[0]);


        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(context, dados_codigos, dados_horas, dados_id);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private boolean validarFormulario() {
        boolean retorno = true;

        if (editNumeroBarril.length() != 6) {
            editNumeroBarril.setError("Quantidade de caracteres inválida");
            editNumeroBarril.requestFocus();
            retorno = false;
        }

        return retorno;
    }

    public void criarPdf(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATEPDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATEPDF) {
            if (data.getData() != null) {
                Uri caminhDoArquivo = data.getData();

                List<Nota> notas = dbController.listarOrdemCrescente();
//                List<Nota> notas = listaDeNotasParaTestes();
                int pageWidth = 1240;
                int pageHeigth = 1754;
                int tamanho = notas.size() - 1;
                int pagina = 1;
                int posicaoY = 120;
                int qtPaginas = (int) Math.ceil(((((double) notas.size() * 20)) / pageHeigth) / 4);

                int posicaoX = 50;

                int coluna = 0;

                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                int cont = 0;
                int contItems = 1;
                for (int i = 0; i < qtPaginas; i++) {
                    posicaoY = 120;
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeigth, pagina).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(24f);
                    paint.setFakeBoldText(true);
                    canvas.drawText("Anotações de barris " + LocalDateTime.now().format(FORMATAR_DATA_PARA_PDF), (float) pageWidth / 2, 60, paint);
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize(16f);
                    paint.setFakeBoldText(false);

                    canvas.drawText("Qt", posicaoX, 100, paint);
                    canvas.drawText("Código", posicaoX + 40, 100, paint);
                    canvas.drawText("Horário", posicaoX + 110, 100, paint);
                    canvas.drawText("Qt", posicaoX + 230, 100, paint);
                    canvas.drawText("Código", posicaoX + 270, 100, paint);
                    canvas.drawText("Horário", posicaoX + 350, 100, paint);
                    canvas.drawText("Qt", posicaoX + 470, 100, paint);
                    canvas.drawText("Código", posicaoX + 510, 100, paint);
                    canvas.drawText("Horário", posicaoX + 590, 100, paint);
                    canvas.drawText("Qt", posicaoX + 710, 100, paint);
                    canvas.drawText("Código", posicaoX + 750, 100, paint);
                    canvas.drawText("Horário", posicaoX + 830, 100, paint);
                    canvas.drawText("Qt", posicaoX + 955, 100, paint);
                    canvas.drawText("Código", posicaoX + 995, 100, paint);
                    canvas.drawText("Horário", posicaoX + 1075, 100, paint);
                    canvas.drawLine(50, 100 + 4, pageWidth - 50, 100 + 4, paint);

                    coluna = 0;
                    while (coluna < 5) {
                        if (coluna == 0) {
                            for (int j = 0; j < 81; j++) {
                                if (cont <= tamanho) {
                                    canvas.drawText("" + contItems, posicaoX, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getCodigo(), posicaoX + 40, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getHora(), posicaoX + 110, posicaoY, paint);
                                    canvas.drawLine(50, posicaoY + 4, pageWidth - 50, posicaoY + 4, paint);
                                    cont++;
                                    contItems++;
                                } else {
                                    break;
                                }

                                posicaoY += 20;
                            }
                            coluna++;
                        } else if (coluna == 1) {
                            posicaoY = 120;
                            for (int j = 0; j < 81; j++) {
                                if (cont <= tamanho) {
                                    canvas.drawText("" + contItems, posicaoX + 230, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getCodigo(), posicaoX + 270, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getHora(), posicaoX + 350, posicaoY, paint);
                                    canvas.drawLine(50, posicaoY + 4, pageWidth - 50, posicaoY + 4, paint);
                                    cont++;
                                    contItems++;
                                } else {
                                    break;
                                }

                                posicaoY += 20;
                            }
                            coluna++;
                        } else if (coluna == 2) {
                            posicaoY = 120;
                            for (int j = 0; j < 81; j++) {
                                if (cont <= tamanho) {
                                    canvas.drawText("" + contItems, posicaoX + 470, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getCodigo(), posicaoX + 510, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getHora(), posicaoX + 590, posicaoY, paint);
                                    canvas.drawLine(50, posicaoY + 4, pageWidth - 50, posicaoY + 4, paint);
                                    cont++;
                                    contItems++;
                                } else {
                                    break;
                                }

                                posicaoY += 20;
                            }
                            coluna += 1;
                        } else if (coluna == 3) {
                            posicaoY = 120;
                            for (int j = 0; j < 81; j++) {
                                if (cont <= tamanho) {
                                    canvas.drawText("" + contItems, posicaoX + 710, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getCodigo(), posicaoX + 750, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getHora(), posicaoX + 830, posicaoY, paint);
                                    canvas.drawLine(50, posicaoY + 4, pageWidth - 50, posicaoY + 4, paint);
                                    cont++;
                                    contItems++;
                                } else {
                                    break;
                                }

                                posicaoY += 20;
                            }
                            coluna += 1;
                        } else {
                            posicaoY = 120;
                            for (int j = 0; j < 81; j++) {
                                if (cont <= tamanho) {
                                    canvas.drawText("" + contItems, posicaoX + 955, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getCodigo(), posicaoX + 995, posicaoY, paint);
                                    canvas.drawText(notas.get(cont).getHora(), posicaoX + 1075, posicaoY, paint);
                                    canvas.drawLine(50, posicaoY + 4, pageWidth - 50, posicaoY + 4, paint);
                                    cont++;
                                    contItems++;
                                } else {
//                                    break;
                                }

                                posicaoY += 20;
                            }
                            coluna += 1;
                        }
                    }
                    pdfDocument.finishPage(page);
                    pagina++;
                }

                // Metodo que finaliza tudo e grava o pdf no arquivo
                gravarPdf(caminhDoArquivo, pdfDocument);

            }
        }
    }

    private void gravarPdf(Uri caminhDoArquivo, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(caminhDoArquivo)));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "PDF Gravado Com Sucesso", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Erro de arquivo não encontrado", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro de entrada e saída", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erro desconhecido" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public List<Nota> listaDeNotasParaTestes() {
        List<Nota> notas = new ArrayList<>();
        Nota nota;

        for (int i = 0; i < 900; i++) {
            nota = new Nota();

            nota.setId(i);
            nota.setCodigo(String.valueOf(i));
            nota.setHora(i + ":99:22");
            notas.add(nota);
        }

        return notas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == R.id.menu_deletar_tudo) {
            // Alert dialog
            AlertDialog.Builder confirmaEsclusao = new AlertDialog.Builder(MainActivity.this);
            confirmaEsclusao.setTitle("Atemção!");
            confirmaEsclusao.setMessage("Esta ação não poderá ser revertida. Deseja continuar?");
            confirmaEsclusao.setCancelable(false);
            confirmaEsclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbController.deletarTudo(DBTabela.TABLE_NAME);
                    preencherListView();
                    preencherQuantidadeDeBarris();

                    Toast.makeText(getApplicationContext(), "Dados apagados com sucesso", Toast.LENGTH_SHORT).show();

                }
            });

            confirmaEsclusao.setNegativeButton("Não", null);

            confirmaEsclusao.create().show();
        } else if (menuId == R.id.menu_gerar_pdf) {
            criarPdf("barris_" + LocalDateTime.now().format(FORMATAR_DATA_PARA_NOME));
//            Toast.makeText(getApplicationContext(), "Gerando PDF", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressLint("SetTextI18n")
    public static void preencherQuantidadeDeBarris() {
        List<Nota> qtNotas = dbController.listarController();

        txtQuantidadeDeBarris.setText("Quantidade: " + qtNotas.size());
    }

}