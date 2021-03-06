package hashmap;

public class GetHashMapLocal {

    private static final int INITIAL_CAPACITY = 16;

    private static final float DEFAULT_FACTOR = 0.75f;

    /**
     * 最大容量
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {

        String key = "Zhang";
        int capacity = 16;
        System.out.println("key: " + key + " ,capacity: " + capacity);
        //计算key在hashmap里面的hash值
        int hash = getHashMapHash(key);
        System.out.println("hash: " + hash);
        int index = getIndexByHashCode(hash, capacity);
        System.out.println("index: " + index);

        System.out.println("calculate index via getHashMapIndexSimplest: " + getHashMapIndexSimplest(key));

        System.out.println("calculate index via getHashMapIndex: key = " + key + " , size = 5 , loadFactor = default . Index is: " + getHashMapIndex(key, 5, 0));

        System.out.println("calculate index via getHashMapIndex: key = " + key + " , size = 14 , loadFactor = default . Index is: " + getHashMapIndex(key, 14, 0));

        System.out.println("calculate index via getHashMapIndex: key = " + key + " , size = 14 , loadFactor = 0.9 . Index is: " + getHashMapIndex(key, 14, 0.9f));

    }

    /**
     * 通过给定的key获得hashmap的index
     * 最简单的，只有初始size的方式
     *
     * @param key
     * @return
     */
    public static int getHashMapIndexSimplest(String key) {
        int hash = getHashMapHash(key);
        int index = getIndexByHashCode(hash, INITIAL_CAPACITY);
        return index;
    }

    /**
     * 比较正式的模拟，通过key，size和loadfactor获得hashmap的index
     *
     * @param key
     * @param size
     * @param loadFactor
     * @return
     */
    public static int getHashMapIndex(String key, int size, float loadFactor) {
        int hash = getHashMapHash(key);
        int capacity = getCapacityFromSize(size, loadFactor);
        int index = getIndexByHashCode(hash, capacity);
        return index;
    }


    /**
     h1 hashcode: 96354
     h2 hashcode: 96354
     */
    //所以结论是，只要String的值一样，则hashcode一样

    /**
     * String hashCode()源码：
     * <p>
     * public int hashCode() {
     * int h = hashmap;
     * if (h == 0 && value.length > 0) {
     * char val[] = value;
     * <p>
     * for (int i = 0; i < value.length; i++) {
     * h = 31 * h + val[i];
     * }
     * hashmap = h;
     * }
     * return h;
     * }
     */
    //但是，object的hashCode肯定不这样，因为object，即使它里面的值相同，但是不同的object对应的hashcode也不一样
    //毕竟在内存里，也存储在不同的地方，又不是引用，对吧，hashcode值一样不就乱了=。=

    //跑了几遍，事实证明这段代码完全不对！！！！！

//    public static int getHashMapTableIndex(String key, int length) {
//        if (key != null) {
//            return key.hashCode() & (length - 1);
//        } else {
//            return 0;
//        }
//    }

    /**
     * 根据hash值取key在hashmap中的index，就是hash除以hashmap的容量，取余
     *
     * @param hashCode
     * @param length
     * @return
     */
    public static int getIndexByHashCode(int hashCode, int length) {
        return hashCode & (length - 1);
    }

    /**
     * 获得一个String的hashcode
     *
     * @param value
     * @return
     */
    public static int getStringHashCode(String value) {
        int hash = 0;
        if (value != null && value.length() > 0) {
            char[] val = value.toCharArray();

            for (int i = 0; i < val.length; i++) {
                hash = 31 * hash + val[i];//int+char强转成int
            }
        }
        return hash;
    }

    //这步计算后没变化，忽略吧=。=
    public static int roundUpToPowerOf2(int number) {
        return number >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
    }

    /**
     * HashMap获得Hash的方法=。=
     * 和正常的hash完全不一样好不=。=
     *
     * @param key
     * @return
     */
    public static int getHashMapHash(Object key) {
        int hash = 0;
        int keyHash;
        if (key instanceof String) {
            //一般hashmap的key都是string，初始取hash就是按照string取hash的方法
            keyHash = getStringHashCode(String.valueOf(key));
        } else {
            keyHash = key.hashCode();
        }
        //然后再按hashmap的特有方法，无符号右移+异或，计算出hashmap中key的hash值
        hash ^= keyHash;//0和任何数异或都等于那个数的原值，所以这步就是赋值
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        return hash ^ (hash >>> 7) ^ (hash >>> 4);
    }

    public static String getBinaryValue(int key) {
        String binary = Integer.toBinaryString(key);
        return binary;
    }

    /**
     * 获得容量阈值
     *
     * @param capacity
     * @param loadFactor
     * @return
     */
    public static int getThreshold(int capacity, float loadFactor) {
        //阈值=容量*加载因子
        int threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        return threshold;
    }

    /**
     * 根据size和加载因子（容积率）获得table[]数组的当前容量
     *
     * @param size
     * @param loadFactor
     * @return
     */
    public static int getCapacityFromSize(int size, float loadFactor) {
        if (loadFactor <= 0) {
            loadFactor = DEFAULT_FACTOR;
        }
        int nowCapacity = INITIAL_CAPACITY;
        int threshold = getThreshold(nowCapacity, loadFactor);
        while (size > threshold) {
            //如果map大小大于阈值，则扩容
            //新容量=当前容量*2
            nowCapacity = nowCapacity * 2;
            //重新计算阈值
            threshold = getThreshold(nowCapacity, loadFactor);
        }
        return nowCapacity;
    }


}
