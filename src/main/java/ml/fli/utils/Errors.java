package ml.fli.utils;

public class Errors {
    /**
     * Выбросить переданное исключение, никак не модифицируя его, но чтобы компилятор думал, что это RuntimeException.
     * <p>
     * Использован этот трюк: http://stackoverflow.com/questions/13528624/utility-class-that-re-throws-a-throwable-as-unchecked
     * /13528805#13528805
     * <p>
     * Смысл в том, что вызов метода throu из метода asUnchecked обманывает компилятор, заставляя его думать, что выброшен будет
     * RuntimeException. А на самом деле выброшено будет именно то исключение, которое передано в параметре. Это возможно благодаря тому,
     * что при выполнении оператора throw проверка типа не осуществляется. Каст исключения в методе throu должен быть именно к дженерику,
     * потому-что так он не попадет в байт-код, в отличие от явного каста к RuntimeException.
     * <p>
     * Недостатки использования этого метода: <br>
     * - автор вышестоящего кода очень удивится, получив checked exception из вашего метода, в сигнатуре которого нет throws;<br>
     * - в отличие от оператора throw, вызов метода asUnchecked не означает для компилятора выхода из текущего метода, поэтому использовать
     * его надо вместе с оператором throw: "throw Errors.asUnchecked(e)".
     * <p>
     *
     * @param exception исключение
     * @return ничего не вернет, поскольку выкинет исключение, но типом возвращаемого значения указан RuntimeException, чтобы можно было
     *         написать "throw Errors.asUnchecked(e)", явно дав компилятору и читающему код понять, что эта строка выбрасывает исключение
     */
    public static RuntimeException asUnchecked(Throwable exception) {
        return Errors.<RuntimeException>throu(exception);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> E throu(Throwable t) throws E {
        throw (E) t;
    }
}
