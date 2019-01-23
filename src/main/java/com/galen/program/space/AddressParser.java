package com.galen.program.space;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by baogen.zhang on 2018/12/8
 *
 * @author baogen.zhang
 * @date 2018/12/8
 */
public class AddressParser {
    public static final char CHAR_ESCAPE = '\\';
    public static final char CHAR_TEST = '#';
    public static final char CHAR_DATA = '*';
    public static final char CHAR_LOCAL_DATA = '~';
    public static final char CHAR_SPLIT = '$';
    public static final char CHAR_LOCATE = '&';

    public static final char CHAR_PATH_ARRAY_LEFT = '[';
    public static final char CHAR_PATH_ARRAY_RIGHT = ']';
    public static final char CHAR_PATH_DOT = '.';

    public static List<Address> parseAddress(String name) {
        List<String> values = split(name);
        List<Address> addresses = new ArrayList<Address>();
        Address root = null;
        Address temp = null;
        for (String path : values) {
            char type = path.charAt(0);
            if (type == CHAR_SPLIT && root != null) {
                addresses.add(root);
                root = null;
                temp = null;
                if (path.length() == 1) {
                    continue;
                }
                path = path.substring(1);
                type = path.charAt(0);
            }
            if (type == CHAR_PATH_ARRAY_RIGHT) {
                continue;
            }
            Address t = buildAddress(path);
            if (root == null) {
                root = t;
                temp = t;
            } else {
                temp.setNext(t);
                temp = t;
            }
        }
        if (root != null) {
            addresses.add(root);
        }
        return addresses;
    }

    public static Address buildAddress(String name) {
        char c = name.charAt(0);
        if (CHAR_LOCATE == c) {
            return new SpaceAddress(name.substring(1));
        } else if (CHAR_TEST == c) {
            return new TestAddress(name);
        } else if (CHAR_DATA == c) {
            return new DataAddress(name);
        } else if (CHAR_LOCAL_DATA == c) {
            return new LocalAddress(name);
        } else if (CHAR_PATH_DOT == c) {
            return new PropertyAddress(name.substring(1), true);
        } else if (CHAR_PATH_ARRAY_LEFT == c) {
            return new IndexAddress(name.substring(1));
        } else {
            return new PropertyAddress(name, false);
        }
    }

    public abstract static class AbstractAddress implements Address {

        private String name;
        private Address next;

        public AbstractAddress(String name) {
            this.name = name;
        }

        @Override
        public Address setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Address setNext(Address next) {
            validNext(next);
            this.next = next;
            return this;
        }

        protected abstract void validNext(Address next);

        @Override
        public Address getNext() {
            return next;
        }

        @Override
        public Address getObject() {
            if (isObject()) {
                return this;
            } else {
                return getNext() != null ? getNext().getObject() : null;
            }
        }

        @Override
        public Address getProperty() {
            if (isProperty()) {
                return this;
            } else {
                return getNext() != null ? getNext().getProperty() : null;
            }
        }

        @Override
        public boolean isSpace() {
            return false;
        }

        @Override
        public boolean isObject() {
            return false;
        }

        @Override
        public boolean isProperty() {
            return false;
        }

        @Override
        public boolean isIndex() {
            return false;
        }

        @Override
        public boolean isInclude(Address sub) {
            return startWith(sub) || (getNext() != null && getNext().isInclude(sub));
        }

        @Override
        public boolean startWith(Address obj) {
            return this.getClass().equals(obj.getClass()) && name.equals(obj.getName()) && ((obj.getNext() == null) || (next != null && next.startWith(obj.getNext())));
        }

        @Override
        public boolean endWith(Address obj) {
            return equals(obj) || (getNext() != null && getNext().endWith(obj));
        }

        @Override
        public Address truncate(Address prefix) {
            if (prefix == null) {
                return this;
            }
            if (name.equals(prefix.getName()) && this.getClass().equals(prefix.getClass()) && getNext() != null) {
                return getNext().truncate(prefix.getNext());
            } else {
                return null;
            }
        }

        @Override
        public int hashCode() {
            return name.hashCode() + next.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            boolean result = this.getClass().equals(obj.getClass()) && name.equals(((Address) obj).getName());
            return result && ((next == null && ((Address) obj).getNext() == null) || (next != null && ((Address) obj).getNext() != null && next.equals(((Address) obj).getNext())));
        }

        @Override
        public String toString() {
            return getString() + (next != null ? next.toString() : "");
        }

        protected String getString() {
            return name;
        }
    }

    public static class SpaceAddress extends AbstractAddress {

        public SpaceAddress(String name) {
            super(name);
        }

        @Override
        protected void validNext(Address next) {
            if (!next.isSpace() && !next.isObject()) {
                throw new NameParseException("{当前：" + this + "后续：" + next + "}格式错误：SPACE后只能跟SPACE或OBJECT！");
            }
        }

        @Override
        public boolean isSpace() {
            return true;
        }

        @Override
        protected String getString() {
            return CHAR_LOCATE + super.getString();
        }
    }

    public static class ObjectAddress extends AbstractAddress {
        public ObjectAddress(String name) {
            super(name);
        }

        @Override
        protected void validNext(Address next) {
            if (!next.isProperty() && !next.isIndex()) {
                throw new NameParseException("{当前：" + this + "后续：" + next + "}格式错误：OBJECT后只能跟PROPERTY或INDEX！");
            }
        }

        @Override
        public boolean isObject() {
            return true;
        }
    }

    public static class TestAddress extends ObjectAddress {
        public TestAddress(String name) {
            super(name);
        }

    }

    public static class DataAddress extends ObjectAddress {
        public DataAddress(String name) {
            super(name);
        }
    }

    public static class LocalAddress extends ObjectAddress {
        public LocalAddress(String name) {
            super(name);
        }
    }

    public static class PropertyAddress extends AbstractAddress {
        private boolean isWithDot;

        public PropertyAddress(String name, boolean isWithDot) {
            super(name);
            this.isWithDot = isWithDot;
        }

        @Override
        protected void validNext(Address next) {
            if (!next.isProperty() && !next.isIndex()) {
                throw new NameParseException("{当前：" + this + "后续：" + next + "}格式错误：PROPERTY后只能跟INDEX或PROPERTY！");
            }
        }

        @Override
        public boolean isProperty() {
            return true;
        }

        @Override
        protected String getString() {
            return (isWithDot ? CHAR_PATH_DOT : "") + super.getString();
        }
    }

    public static class IndexAddress extends AbstractAddress {
        public IndexAddress(String name) {
            super(name);
        }

        @Override
        protected void validNext(Address next) {
            if (!next.isProperty() && !next.isIndex()) {
                throw new NameParseException("{当前：" + this + "后续：" + next + "}格式错误：INDEX后只能跟INDEX或PROPERTY！");
            }
        }

        @Override
        public boolean isIndex() {
            return true;
        }

        @Override
        protected String getString() {
            return CHAR_PATH_ARRAY_LEFT + super.getString() + CHAR_PATH_ARRAY_RIGHT;
        }
    }


    /**
     * 将字符串按特殊字符分隔，支持对特殊字符的转义处理。
     *
     * @param value
     * @return
     */
    public static List<String> split(String value) throws NameParseException {
        char[] delimitChars = {CHAR_LOCATE, CHAR_TEST, CHAR_DATA, CHAR_LOCAL_DATA, CHAR_PATH_DOT, CHAR_PATH_ARRAY_LEFT, CHAR_PATH_ARRAY_RIGHT, CHAR_SPLIT};
        String delimits = new String(delimitChars);
        char escape = CHAR_ESCAPE;
        char split = CHAR_SPLIT;
        char arrayRight = CHAR_PATH_ARRAY_RIGHT;
        char arrayLeft = CHAR_PATH_ARRAY_LEFT;
        char dot = CHAR_PATH_DOT;
        String allChars = delimits + escape;

        List<String> result = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(value, allChars, true);
        StringBuilder beforeValue = new StringBuilder();
        boolean isEscape = false;
        boolean isDelimit = false;
        boolean isSplit = false;
        boolean isArrayRight = false;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            boolean isSplitBefore = isSplit;
            //设置是否分隔符，分隔符后可以跟其他特殊符
            if (!isEscape && token.length() == 1 && token.charAt(0) == split) {
                isSplit = true;
            } else {
                isSplit = false;
            }

            boolean isArrayRightBefore = isArrayRight;
            //设置是否]符，]符后可以跟点号.。
            if (!isEscape && token.length() == 1 && token.charAt(0) == arrayRight) {
                isArrayRight = true;
            } else {
                isArrayRight = false;
            }
            if (isArrayRightBefore && !(token.length() == 1 && token.charAt(0) == dot)) {
                throw new NameParseException("字符串\"" + value + "\"格式错误：]后面必须是.");
            }

            if (isEscape && isDelimit) {//上一个特殊符是界限符同时是转义符
                //如果是特殊符，被转义。否则被分隔。
                if (token.length() == 1 && allChars.contains(token.charAt(0) + "")) {
                    beforeValue.append(token);
                } else {
                    if (beforeValue.length() != 0) {
                        result.add(beforeValue.toString());
                    }
                    beforeValue = new StringBuilder(escape + token);
                }
                isEscape = false;
                isDelimit = false;
            } else if (isEscape) {//上一个特殊符不是界限符而是转义符
                //本次必须是特殊符，被转义
                if (token.length() == 1 && allChars.contains(token.charAt(0) + "")) {
                    beforeValue.append(token);
                    isEscape = false;
                } else {
                    throw new NameParseException("字符串\"" + value + "\"格式错误：转义符后必须是特殊符号！");
                }
            } else if (isDelimit) {//上一个特殊符是界限符不是转义符
                //必须是值，设置到临时变量
                if (token.length() == 1 && allChars.contains(token.charAt(0) + "")) {
                    if (isSplitBefore) {
                        if (token.charAt(0) == split) {
                            throw new NameParseException("字符串\"" + value + "\"格式错误：不能连续两个分隔符号！");
                        } else {
                            if (beforeValue.length() != 0) {
                                result.add(beforeValue.toString());
                            }
                            beforeValue = new StringBuilder(token);
                            isDelimit = true;
                        }
                    } else if (isArrayRightBefore && token.charAt(0) == dot) {
                        if (beforeValue.length() != 0) {
                            result.add(beforeValue.toString());
                        }
                        beforeValue = new StringBuilder(token);
                        isDelimit = true;
                    } else {
                        throw new NameParseException("字符串\"" + value + "\"格式错误：不能连续两个特殊符号！");
                    }
                } else {
                    beforeValue.append(token);
                    isDelimit = false;
                }
            } else {//上一个特殊符没有
                if (token.length() == 1 && allChars.contains(token.charAt(0) + "")) {
                    if (token.charAt(0) == escape) {
                        isEscape = true;
                    }

                    if (delimits.contains(token.charAt(0) + "")) {
                        if (!isEscape) {
                            if (isArrayRight) {
                                if (beforeValue.charAt(0) != arrayLeft) {
                                    throw new NameParseException("字符串\"" + value + "\"格式错误：]未与[配对");
                                } else {
                                    try {
                                        Integer.valueOf(beforeValue.substring(1));
                                    } catch (NumberFormatException e) {
                                        throw new NameParseException("字符串\"" + value + "\"格式错误：[]之间只能是索引数值！");
                                    }
                                }
                            }
                            if (beforeValue.length() != 0) {
                                result.add(beforeValue.toString());
                            }
                            beforeValue = new StringBuilder(token.charAt(0) + "");
                        }

                        isDelimit = true;
                    }
                } else {
                    beforeValue.append(token);
                }
            }
        }
        if (beforeValue.length() != 0) {
            result.add(beforeValue.toString());
        }
        return result;
    }
}
