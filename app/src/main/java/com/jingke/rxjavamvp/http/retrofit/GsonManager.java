package com.jingke.rxjavamvp.http.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;

/**
 * Gson manager
 * Created by wangli on 2016/6/24.
 */
public class GsonManager {

    private static Gson defaultGson;

    public static Gson getDefaultGson(){
        if(defaultGson==null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(String.class, STRING);
            gsonBuilder.registerTypeAdapter(int.class, INTEGER);
            gsonBuilder.registerTypeAdapter(Integer.class, INTEGER);

            //通过反射获取instanceCreators属性
            try {
                Class builder = (Class) gsonBuilder.getClass();
                Field f = builder.getDeclaredField("instanceCreators");
                f.setAccessible(true);
                Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBuilder);//得到此属性的值
                //注册数组的处理器
                gsonBuilder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(new ConstructorConstructor(val)));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            defaultGson = gsonBuilder.create();
        }
        return defaultGson;
    }

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return "";//原先是返回Null，这里改为返回空字符串
                }
                if (reader.peek() == JsonToken.NUMBER){
                    Double v = reader.nextDouble();
                    int intValue = v.intValue();
                    if (v==intValue){//服务端返回的是int，客户端用string接收，但是gson默认会转成double型的string处理（比如1.0），这里再转成int型的string（比如1）
                        return String.valueOf(intValue);
                    }
                    long longValue = v.longValue();
                    if (v==longValue){//服务端返回的是long，客户端用string接收，但是gson默认会转成double型的string处理（比如1.0），这里再转成long型的string（比如1）
                        return String.valueOf(longValue);
                    }
                    return String.valueOf(v);
                }
                return reader.nextString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 自定义adapter，解决由于数据类型为Int,实际传过来的值为Float，导致解析出错的问题
     * 目前的解决方案为将所有Int类型当成Double解析，再强制转换为Int
     */
    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            try {
                double i = in.nextDouble();//当成double来读取
                return (int) i;//强制转为int
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };

    /**
     * 自定义CollectionTypeAdapterFactory，使json内的数组为null时，返回空数组而不是null对象
     */
    /**
     * Adapt a homogeneous collection of objects.
     */
    public static final class CollectionTypeAdapterFactory implements TypeAdapterFactory {
        private final ConstructorConstructor constructorConstructor;

        public CollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
            this.constructorConstructor = constructorConstructor;
        }

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Type type = typeToken.getType();

            Class<? super T> rawType = typeToken.getRawType();
            if (!Collection.class.isAssignableFrom(rawType)) {
                return null;
            }

            Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
            TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
            ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

            @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
                    TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter, constructor);
            return result;
        }

        private final class Adapter<E> extends TypeAdapter<Collection<E>> {
            private final TypeAdapter<E> elementTypeAdapter;
            private final ObjectConstructor<? extends Collection<E>> constructor;

            public Adapter(Gson context, Type elementType,
                           TypeAdapter<E> elementTypeAdapter,
                           ObjectConstructor<? extends Collection<E>> constructor) {
                this.elementTypeAdapter =
                        new TypeAdapterRuntimeTypeWrapper<E>(context, elementTypeAdapter, elementType);
                this.constructor = constructor;
            }

            @Override
            public Collection<E> read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    //这里做了修改，原先是返回null，改为返回空数组
                    return constructor.construct();
                }

                Collection<E> collection = constructor.construct();
                in.beginArray();
                while (in.hasNext()) {
                    E instance = elementTypeAdapter.read(in);
                    collection.add(instance);
                }
                in.endArray();
                return collection;
            }

            @Override
            public void write(JsonWriter out, Collection<E> collection) throws IOException {
                if (collection == null) {
                    out.nullValue();
                    return;
                }

                out.beginArray();
                for (E element : collection) {
                    elementTypeAdapter.write(out, element);
                }
                out.endArray();
            }
        }
    }

    final static class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
        private final Gson context;
        private final TypeAdapter<T> delegate;
        private final Type type;

        TypeAdapterRuntimeTypeWrapper(Gson context, TypeAdapter<T> delegate, Type type) {
            this.context = context;
            this.delegate = delegate;
            this.type = type;
        }

        @Override
        public T read(JsonReader in) throws IOException {
            return delegate.read(in);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public void write(JsonWriter out, T value) throws IOException {
            // Order of preference for choosing type adapters
            // First preference: a type adapter registered for the runtime type
            // Second preference: a type adapter registered for the declared type
            // Third preference: reflective type adapter for the runtime type (if it is a sub class of the declared type)
            // Fourth preference: reflective type adapter for the declared type

            TypeAdapter chosen = delegate;
            Type runtimeType = getRuntimeTypeIfMoreSpecific(type, value);
            if (runtimeType != type) {
                TypeAdapter runtimeTypeAdapter = context.getAdapter(TypeToken.get(runtimeType));
                if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                    // The user registered a type adapter for the runtime type, so we will use that
                    chosen = runtimeTypeAdapter;
                } else if (!(delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                    // The user registered a type adapter for Base class, so we prefer it over the
                    // reflective type adapter for the runtime type
                    chosen = delegate;
                } else {
                    // Use the type adapter for runtime type
                    chosen = runtimeTypeAdapter;
                }
            }
            chosen.write(out, value);
        }

        /**
         * Finds a compatible runtime type if it is more specific
         */
        private Type getRuntimeTypeIfMoreSpecific(Type type, Object value) {
            if (value != null
                    && (type == Object.class || type instanceof TypeVariable<?> || type instanceof Class<?>)) {
                type = value.getClass();
            }
            return type;
        }
    }

}
