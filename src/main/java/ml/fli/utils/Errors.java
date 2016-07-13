package ml.fli.utils;

public class Errors {
    /**
     * Выбросить переданное исключение, никак не модифицируя его, но чтобы компилятор думал, что это RuntimeException.
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
