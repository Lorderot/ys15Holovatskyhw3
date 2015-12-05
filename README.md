# ys15Holovatskyhw3
int Stream with lazy execution


��������� ������ �DD ����������� ��������� ������ ��� ������ AsIntStream �� ������ � �������������
������� (stream) ������. �������� ������� ������ ���� 100%.
�������� ������� ������ ��������� ����.

- Double average()
������� �������� ����� � ������. IllegalArgumentException - if empty

- Integer max()/min()
�������������/������������ �������� ����� � ������. IllegalArgumentException - if empty

- long count()
���������� �������� (���������) � ������

- Integer sum()
����� ���� �������� � ������. IllegalArgumentException - if empty

- int[] toArray()
���������� ����� � ���� �������

- void forEach(IntConsumer action)
��� ������� �������� �� ������ ��������� �������� ��������� � ���������� IntConsumer, ������ �� ����������

- IntStream filter(IntPredicate predicate)
��� ������� �������� �� ������ ��������� ��� �� ������� ������������� �� ��� ������� � ���������� IntPredicate,
���� �� - ���������� ��� � �������������� �����, ���� ��� - �����������

- IntStream map(IntUnaryOperator mapper)
��������� � ������� �� �������� ������ ���������� IntUnaryOperator � ���������� ��� � �������������� �����

- IntStream flatMap(IntToIntStreamFunction func)
��������� � ������� �� �������� ������ ���������� IntToIntStreamFunction, ������� 
�� ������ ������� �� �������� ������� ������ ��������, ������� ������������
� ���� �������������� ����� (��. http://java.amitph.com/2014/02/java-8-streams-api-intermediate.html)

- int reduce(int identity, IntBinaryOperator op)
��������� ������� �������� ������ � ����� �����, ��������� �������� �������� identity, ������� ������� - � ���������� IntBinaryOperator

��� ���������� ����� ������������ ����������� ������������ ������ � �������

� �������� ������ ������� ����������� ��������� ������ Maven-������� (�� ������������� pom.xml � checkstyle.xml) file:streams.zip
� ������ ������� ��� ���� ������� ������� � ��������� ����, ������� ������ ������ ��������� ����� ���������� ����� �������.
���������� �������� ��������� ����� �� ��� ������ ����� AsIntStream.
���������� ������ ������ ���� �������� �� GitHub/BitBucket (����������� ������� �������� ������ git)
� ������ ������ ������� ������ �� Hudson CI (������� ����������� ���� ��������� ����������� ������������ ���� �
100% �������� ���� �������). ����� ����� ������ �� ����������� ������ ���� ��������� �� �����