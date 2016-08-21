import java.lang.reflect.Method;
import java.util.*;

import tel_ran.reflection.x.X;
import tel_ran.reflection.x.X1;
import tel_ran.reflection.x.Y;
import tel_ran.reflection.x.Z;

public class ClassReflectionScenarios {

	private static final String PACKAGE_NAME = "tel_ran.reflection.x";
	public static void main(String[] args) {
		//Scenario #1 - get empty object from object of unknown class
		Collection<Integer> collection = new HashSet<>();
		collection.add(10);
		collection.add(15);
		Collection<Integer> resEven = getEvenNumbers(collection);
		System.out.println(resEven.getClass() == collection.getClass());
		
		//Scenario #2 - get name of the class of an object of unknown class
		X[]array = {new X(), new Z(), new Z(), new X(), new Y(), new X()};
		HashMap<String,Integer> mapX = getMapObjectsX(array);
		System.out.println(mapX);
		
		//Scenario #3 - get object of the class having string with the class name
		HashMap<String,Integer> map = new HashMap<>();
		map.put("X", 3);
		map.put("Y", 2);
		map.put("Z", 1);
		ArrayList<X> arrayX	= getArrayObjectsX(map);
		action(arrayX);
		
		//Scenario #4 - name of method and parameters in string and object of class Class
		Object obj = new X1();
		proceed(obj, "f1", null);
		proceed(obj, "f2", null);
		proceed(obj, "f1", "hello");
	}
	private static void proceed(Object obj, String name, String parameter) {
		Class objClass = obj.getClass();
		Method method = null;
		try {
			method = parameter == null ? objClass.getDeclaredMethod(name) : 
				objClass.getDeclaredMethod(name, String.class);
			method.setAccessible(true);// set flag to invoke private methods
			if(parameter == null)
				method.invoke(obj);
			else
				method.invoke(obj, parameter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void action(ArrayList<X> arrayX) {
		for(X x : arrayX){
			x.action();
		}
	}
	private static ArrayList<X> getArrayObjectsX(HashMap<String, Integer> map) {
		ArrayList<X> res = new ArrayList<>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			createObjects(entry.getKey(),entry.getValue(),res);
		}
		return res;
	}
	private static void createObjects(String className, int count, ArrayList<X> res) {
		for(int i = 0; i < count; i++){
			try {
				X x = (X) Class.forName(PACKAGE_NAME+"."+className).newInstance();//static method - forClass
				res.add(x);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static HashMap<String, Integer> getMapObjectsX(X[] array) {
		// get name of class and counter
		HashMap<String, Integer> res = new HashMap<>();
		for(X x : array){
			String className = x.getClass().getName();
			Integer count = res.get(className);
			if(count == null){
				count = 0;
			}
			count++;
			res.put(className, count);
		}
		return res;
	}													
	private static Collection<Integer> getEvenNumbers(Collection<Integer> collection) {
		//Class<? extends Collection<Integer>> objClass = collection.getClass();//getClass() return object of class Object
		@SuppressWarnings("rawtypes")
		Class objClass = collection.getClass();
		try {
			@SuppressWarnings("unchecked")
			Collection<Integer> res = (Collection<Integer>) objClass.newInstance();
			for(Integer number : collection){
				if(number % 2 == 0)
					res.add(number);
			}
			return res;
		} catch (InstantiationException e) {//нет конструктора по умолчанию
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {//есть конструктор но не паблик
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return null;
	}

}
