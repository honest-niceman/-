package MVP;

import Buissines.Analyzer;
import UI.AnalyzerView;

public class AnalyzerPresenter {
    private Analyzer analyzer;
    private AnalyzerView analyzerView;

    public AnalyzerPresenter(Analyzer analyzer, AnalyzerView analyzerView) {
        this.analyzer = analyzer;
        this.analyzerView = analyzerView;
    }

    public void check(String expression){
        analyzer.setExpression(expression);
        String answer = "";
        if(analyzer.checkExpression() == -1){
            answer = "Конструкция введена некорректно с точки зрения синтаксиса";
        }
        else if(analyzer.checkExpression() == 1){
            answer = ("Конструкция введена корректно и данный цикл выполниться более одного раза");
        }
        else if(analyzer.checkExpression() == 2){
            answer = "Конструкция введена корректно, однако цикл не выполниться более одного раза";
        }
        analyzerView.updateAnswerLabel(answer);
    }
}
