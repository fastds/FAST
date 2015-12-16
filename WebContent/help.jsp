<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>FASTDB</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <link href="css/bootstrap.css" rel="stylesheet">
 <style type="text/css">
   <style type="text/css">
   body {
     padding-top: 60px;
     padding-bottom: 40px;
   }
  div.x { background: #eee; font-family: 'Courier New', monospace !important; white-space: pre; width: 100%; font-size: 0.85em; border: 1px solid #bbbbbb; padding-left:15px;}
 </style>
 <link href="css/bootstrap-responsive.css" rel="stylesheet">
</head>
<body>
<div class="container">
	<jsp:include page="top.jsp"></jsp:include>
      <div class="row">
        <div class="span3">
          <div id="sbar" class="well sidebar-nav">
          </div><!--/.well -->
        </div><!--/span-->
        <div id="ref" class="span9">


<h1>SciDB Operators</h1>
<h2>What is a SciDB operator?</h2>

<p>
SciDB <i>operators</i> map zero or more input
arguments to a SciDB array. The arguments may be a mix of constant 
scalar values, scalar-valued functions,
and SciDB arrays depending on the operator. All
operators produce output arrays. Some operators also produce side-effects by
modifying SciDB system state or saving data to files, etc.
</p><p>
SciDB operators can be composed&mdash;that is, the output of an operator
may provide array input into another operator.
</p>


<hr/>


<a class="op" name="aggregate"></a><h2 >aggregate</h2>
<i>aggregate(array, aggregate_expression1 [as label1] [,aggregate_expression2 as label2, ...] [,dimension1, dimension2, ...])</i>
<p/>Aggregate values through a SciDB aggregation function, optionally grouped
by the specified dimensions. See the index for a list of available aggregation
functions.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, avg(x) as mean, j)

</div><br/>

<a class="op" name="allversions"></a><h2 >allversions</h2>
<i>allversions(array)</i>
<p/>Create an array that contains all the versions of an existing array,
organized along a new dimension called "VersionNo."
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0],i), A)
store(build(&lt;x:double&gt;[i=1:10,10,0],2*i), A)
store(build(&lt;x:double&gt;[i=1:10,10,0],3*i), A)
allversions(A)

</div><br/>



<a class="op" name="apply"></a><h2 >apply</h2>
<i>apply(source_array,new_attribute1,function1[,new_attribute2,function2]...)</i>
<p/>Create an array with new attributes defined by scalar functions of existing attributes and/or constants.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0],i), A)
apply(A, y, sqrt(x) + 1)

# To see a list of available scalar functions, run the query:
list('functions')

</div><br/>


<a class="op" name="ApproxDC"></a><h2 >ApproxDC</h2>
<i>ApproxDC(array[,attribute[,dimension_1,dimension_2,...])
</i>
<p/>Create an array that contains an estimate the number of distinct values of an array attribute,
optionally grouped along one or more dimensions.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0],i/2), A)
ApproxDC(A,x)

</div><br/>

<a class="op" name="attribute_rename"></a><h2 >attribute_rename</h2>
<i>attribute_rename(array,old_attribute1,new_attribute1[, old_attribute2,new_attribute2,...])
</i>
<p/>Create a duplicate array with renamed attributes.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0],i/2), A)
attribute_rename(A,x,z)

</div><br/>

<a class="op" name="attributes"></a><h2 >attributes</h2>
<i>attributes(array)</i>
<p/>Create an array describing attributes of an existing named array.
<br/>
<div class="x">
store(apply(build(&lt;x:double&gt;[i=1:10,10,0],i/2),y,5), A)
attributes(A)

</div><br/>


<a class="op" name="avg_rank"></a><h2 >avg_rank</h2>
<i>avg_rank(array[, attribute[, dimension1[, dimension2, ...]]])</i>
<p/>Create an array with an attribute that ranks an existing array attribute along one or more dimensions, averaging ties. The rank attribute name is the original attribute name plus "_rank." The original array attribute is also returned in the output array.
<br/>
<div class="x">
avg_rank(
  build(&lt;x:double&gt;[i=1:4,4,0,j=1:4,4,0],10*double(random())/2147483647 + 1),
  x, i)

</div><br/>


<a class="op" name="between"></a><h2 >between</h2>
<i>between(array, low_coord1[, low_coord2, ...], high_coord1[, high_coord2, ...])</i>
<p/>
Create a new sparse array <b>of the same shape</b> as the input array with
empty cells outside the specified rectangular coordinate range and copies of
the input array cells elsewhere.
Compare with the <a href="#subarray"><u>subarray</u></a> operator.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0],i), A)
between(A, 3, 7)

</div><br/>


<a class="op" name="build"></a><h2 >build</h2>
<i>build(array | schema, expression)</i>
<p/>Create a single-attribute array with values defined by the expression. If an array is supplied,
it's schema will be used (that is, the first argument is either explicitly or implicitly a schema).
The schema dimensions must have explicit bounds indicated.
<br/>
<div class="x">
build(&lt;x:double&gt;[i=1:10,10,0],sqrt(i))

create_array(A,&lt;x:double&gt;[i=1:10,10,0])
build(A,sqrt(i))

</div><br/>


<a class="op" name="cancel"></a><h2 >cancel</h2>
<i>cancel(query id)</i>
<p/>Cancel the specified active query id.
<br/>


<a class="op" name="cast"></a><h2 >cast</h2>
<i>cast(array, array | schema)</i>
<p/>Duplicate an existing array, changing its schema. The example converts
a string dimension to an integer dimension. Cast can also change dimension
and attribute names.
<br/>
<div class="x">
create_array(A,&lt;i:int64&gt;[x(string)=10,10,0])
redimension_store(build(&lt;x:string&gt;[i=1:10,10,0],'x'+string(i)), A)
cast(A,&lt;i:int64&gt;[x=0:9,10,0])

</div><br/>



<a class="op" name="cross_join"></a><h2 >cross_join</h2>
<i>cross_join(array1 [as label1], array2 [as label2] [, dimension1, dimension2[, dimension3, dimension4, ...]])
</i>
<p/>
Create the cross-product array of two arrays with equality predicates applied to
pairs of dimensions. The arrays must have int64 dimension types. Use the
'as' keyword to label arrays for reference within the query (see the example).
<br/>
<div class="x">
cross_join(build(&lt;x:double&gt;[i=1:10,5,0,j=0:2,1,0],1) as A,
           build(&lt;y:double&gt;[i=1:10,5,0],2) as B, A.i, B.i)

</div><br/>


<a class="op" name="dimensions"></a><h2 >dimensions</h2>
<i>dimensions(array)</i>
<p/>Create an array that describes the dimensions of the specified array.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,5,0,j=0:2,1,0],1), A)
dimensions(A)
</div><br/>


<a class="op" name="filter"></a><h2 >filter</h2>
<i>filter(array, boolean_expression)</i>
<p/>Create an array of the same shape as the specified array that copies cells that
meet the Boolean expression and marks cells that don't meet the condition EMPTY. The
expression may use attributes and/or dimensions.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,5,0,j=0:2,1,0],5), A)
filter(A, x + i < 10)

</div><br/>


<a class="op" name="input"></a><h2 >input</h2>
<i>input(schema, input_file, instance_id, format [, max_num_errors])</i>
<p>
Load data from a file, creating a new array.
The <i>schema</i> parameter defines the data schema for the input array.
The <i>input_file</i> parameter is a file path resolvable on specified instances.
The <i>instance_id</i> parameter controls which instance(s) load the file as follows:
<ol>
<li value="-2"> Load all data using the coordinator instance of the query.
<li value="-1"> Initiate the load from all instances. That is, the load is distributed to all instances, and the data is loaded concurrently.
<li value="0"> Load all data using the specified instance ID 0.
<li value="5"> Load all data using the specified instance ID 5, etc. ...
</ol>
The <i>format</i> parameter is a character string specifying the file format--see the full documentation for examples. Set <i>format</i> to an empty character string to indicate default SciDB ASCII format.
</p>
The optional <i>max_num_errors</i> parameter specifies the maximum number of tolerated load errors with a default value of zero.

<br/><br/>
<div class="x">
save(build(&lt;x:double&gt;[i=1:3,3,0,j=1:3,3,0],i+j),'/tmp/example.bin', 0, '(double)')
input(&lt;v:double&gt;[m=1:3,3,0,n=1:3,3,0],'/tmp/example.bin',-2,'(double)')

</div><br/>


<a class="op" name="insert"></a><h2 >insert</h2>
<i>insert(input, array)</i>
<p/>Insert specified input values into an existing named array
(the output is a new version number of the named array input).
This operator has the side effect of also updating the original named array with
the inserted values. The arrays must have identical schema.
Insert is essentially a fast version of <tt>store(merge(X,Y),Y)</tt>.
<br/>
<div class="x">
store(build_sparse(&lt;x:double&gt;[i=1:10,5,0], sqrt(i), double(i)/2 = i/2),A)
insert(build(&lt;x:double&gt;[i=1:10,5,0],i),A)

</div><br/>


<a class="op" name="join"></a><h2 >join</h2>
<i>join(array1, array2)</i>
<p/>Create an array that joins attributes from non-empty cells of two arrays at matching dimension values.
The arrays must have the same dimension starting coordinates, chunk sizes and chunk overlaps.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,5,0], sqrt(i)),A)
join(build(&lt;y:double&gt;[i=1:50,5,0], i*i),A)

</div><br/>


<a class="op" name="list"></a><h2 >list</h2>
<i>list(element)</i>
<p/>Create an array that lists the requested elements in SciDB. 'element' is a string value that may be one of:
<ul>
<li>'arrays'
<li>'aggregates'
<li>'functions'
<li>'operators'
<li>'types'
<li>'queries'
<li>'instances'
<li>'libraries'
</ul>
Leaving element unspecified implies list('arrays').
<br/>
<div class="x">
list('arrays')

</div><br/>


<a class="op" name="merge"></a><h2 >merge</h2>
<i>merge(array1, array2)</i>
<p/>Create an array that merges the contents of two arrays. The arrays must
have the same number of attributes, attribute types, starting dimension values, chunk sizes
and chunk overlaps. Non-empty cells from "array1" appear in the output, as do
non-empty cells from "array2" that correspond to empty cells in "array1." Both arrays
must have int64 dimension types.
<br/>
<div class="x">
store(build_sparse(&lt;x:double&gt;[i=1:10,5,0], sqrt(i), double(i)/2 = i/2), A)
merge(A, build(&lt;x:double&gt;[i=1:10,5,0], i))

</div><br/>



<a class="op" name="project"></a><h2 >project</h2>
<i>project(array, attribute[, attribute[,...]])</i>
<p/>Create a duplicate of the specified array restricted to
the listed attributes.
<br/>
<div class="x">
store(apply(build(&lt;val:double&gt;[i=0:10,10,0],cos(i/3)), y, sin(i/3)), A)
project(A, y)

</div><br/>



<a class="op" name="rank"></a><h2 >rank</h2>
<i>rank(array[, attribute[, dimension1[, dimension2, ...]]])</i>
<p/>Create an array that assigns rank order to elements of specified attribute.
The rank attribute name is the original attribute name plus "_rank." The original array attribute is also returned in the output array.
<br/>
<div class="x">
rank(
  build(&lt;x:double&gt;[i=1:10,10,0], random()%7/1.0),
x) 

</div><br/>


<a class="op" name="redimension"></a><h2 >redimension</h2>
<i>redimension(source, schema[, aggregate (source_attribute) [as result_attribute]]...)
</i>
<p/>
The <i>redimension</i> operator is a non-storing version of
<i>redimension_store</i> described next. It creates an array that omits
attributes or dimensions, promotes attributes from "source" as dimensions, or
demotes dimensions from the "source" array to attributes, according to the
"schema" parameter, optionally applying aggregates as it goes.
<br/><br/>
The "schema" parameter may only contain int64 dimension types, unlike
more general <i>redimension_store</i> operator described next.
<br/><br/>
The example switches an integer dimension in the build array into
an integer attribute in the output array.
<div class="x">
redimension(build(&lt;k:int64&gt;[i=1:10,10,0],i-100),&lt;i:int64&gt;[k=-99:*,10,0])

</div><br/>




<a class="op" name="regrid"></a><h2 >regrid</h2>
<i>regrid(array, grid_1[, grid2[, ...]], aggregate1[, aggregate2[, ...]])</i>
<br/>
<p>
Create an array that partitions the cells in the input array into blocks, and
for each block, apply an aggregate operation over the values in the block.
Regrid may only be applied to int64 integer dimensions. That means each grid
argument must correspond to an integer-valued (of type int64) dimension, or be
1 (implying no aggregation over that dimension). The output array dimension
values begin at the same starting values as the input array, counting
sequentially up to the total number of grid divisions in each dimension.
</p>
<br/>
The example produces a 5x5 array that sums values and computes the maximum value over 3x2 blocks from the input array.
<div class="x">
store(build(&lt;x:double&gt;[i=2:10,10,0,j=1:10,10,0],10*double(random())/2147483647 + 1),A)
regrid(A, 3, 2, sum(x) as sum, max(x) as max)

# Recover original dimension values along the regridded data:
apply(
  regrid(A, 3, 2, sum(x) as sum, max(x) as max),
  i_original, 2 + (i-2)*3,
  j_original, 1 + (j-1)*2
)

</div><br/>



<a class="op" name="rename"></a><h2 >rename</h2>
<i>rename(array, new_name)</i>
<p/>Rename an array. The new name is specified without quoting. This
operator returns nothing, and is only used for its side effect.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:10,10,0],i+j),A)
rename(A, A1)

</div><br/>


<a class="op" name="repart"></a><h2 >repart</h2>
<i>repart(array1, array2 | schema)</i>
<p/>Duplicate the contents of array1, applying the schema
defined in array2 or schema. The schema must match attribute
and dimension names and types from array1, but is free to define
chunk size, chunk overlap, and dimension upper bounds.
The operator returns the duplicated
array and, if an output array is specified as array2, also stores
its output there.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:10,10,0],i+j),A)
repart(A, &lt;x:double&gt;[i=1:10,5,1,j=1:10,2,0])

</div><br/>

<a class="op" name="reshape"></a><h2 >reshape</h2>
<i>reshape(array1, array2 | schema)</i>
<p/>Duplicate the contents of array1 into the schema defined by
the specified output array2 or schema. The schema must match
the attributes of the source array1, and its dimensions must
define an array with the same number of cells as array1.
The reshape operator does not work on arrays with nonzero
chunk overlap.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:10,10,1],i+j),A)
reshape(A, &lt;x:double&gt;[i=1:100,10,0])
reshape(A, &lt;x:double&gt;[i=1:5,5,0,j=1:5,5,0,k=1:4,4,0])

</div><br/>



<a class="op" name="save"></a><h2 >save</h2>
<i>save(array, path[, instance[, format]])</i>
<p/>Save an array to the indicated path, returning the array to the caller.
The instance indicates on which SciDB instance filesystem to save the
file (see <tt>list('instances')</tt>&mdash; 0 means the coordinator instance).
Format is a string that indicates the saved file format. Here are some
possible options (see the SciDB reference guide for more):
<br/><br/>
<table class='tbl'>
<tr><td>Format string</td><td>Description</td></tr>
<tr><td>'lcsv+'</td><td>Dimension and attribute values separated by commas</td></tr>
<tr><td>'dcsv'</td><td>Dimension values in braces separated by commans, then attribute values separated by commas</td></tr>
<tr><td>'(type1[, type2[, ...]])'</td><td>Attribute values in binary save format (see below).</td></tr>
</table>
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:3,3,0,j=1:3,3,0],i+j),A)
save(A, '/tmp/A.csv', 0, 'lcsv+')
save(A, '/tmp/A.bin', 0, '(double)')

</div><br/>
Examples of reading the output files produced by the example using
GNU command-line utilities follow:
<br/>
<br/>
<div class="x">
cat /tmp/A.csv           # ASCII comma-separated value format
od -tf8 /tmp/A.bin       # Binary output format

</div>


<a class="op" name="scan"></a><h2 >scan</h2>
<i>scan(array)</i>
<p/>Identity function&mdash;return the array.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:3,3,0,j=1:3,3,0],i+j),A)
scan(A)

</div><br/>


<a class="op" name="show"></a><h2 >show</h2>
<i>show(array | schema | SciDB query expression[, 'afl'])</i>
<p/>
Create an array containing the schema of the specified stored
array, schema or SciDB query expression. When a SciDB query is
specified, use the optional 'afl' argument if the query is in
AFL format. The default assumes queries are in AQL format.
<br/>
<div class="x">
show('sort(build(&lt;x:double&gt;[i=1:3,3,0],random()))', 'afl')

</div><br/>


<a class="op" name="slice"></a><h2 >slice</h2>
<i>slice(array, dimension1, value1[, dimension2, value2[, ...]])</i>
<p/>
<br/>Create a new array that subsets the input array
along specified dimension values. The output array has lower
dimension than the input array.
<div class="x">
store(build(&lt;x:double&gt;[i=1:3,3,0,j=1:3,3,0],i+j),A)
slice(A, i, 2)

</div><br/>


<a class="op" name="sort"></a><h2 >sort</h2>
<i>sort(array, attribute [asc | desc][, attribute [asc | desc][, ...]][, chunk_size])</i>
<p/>
<p>
Create an array containing a sorted representation of the input array by
attribute values, optionally specifying ascending or descending order and
output chunk size. The output array is one-dimensional with a single signed
int64 dimension "n" (don't use sort on an input array attribute named "n"
or you will get a name collision!).
<p>
If you don't specify an output array chunk size, then the output will have
a chunk size equal to the extent of the data or one million, whichever is
smaller.
</p><p>
The sorted output array will have zero chunk overlap.
</p>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0],10*double(random())/2147483647 + 1),A)
store(apply(A, s, 'x' + string(x)), B)
sort(B, x, s)

</div><br/>


<a class="op" name="store"></a><h2 >store</h2>
<i>store(result, array)</i>
<p/>Store the output result of a SciDB operator to the array name
specified, returning the output array for use by the caller.
<br/>
<div class="x">
sum(store(build(&lt;x:double&gt;[i=1:10,10,0],i), A))

store(build(&lt;x:double&gt;[i=1:10,10,0],i), A)
sum(A)

</div><br/>


<a class="op" name="subarray"></a><h2 >subarray</h2>
<i>subarray(array, low_coord1[, low_coord2[, ...]], high_coord1[, high_coord2[, ...]])</i>
<p/>
<p>
Create an array containing a clipped, rectilinear subset of the
input array. The coordinates specified must, in order, indicate the
low boundaries in each array dimension, followed by the high boundaries
in each array dimension. Use the value <i>null</i> for a low or high coordinate
to use the minimum and maximum coordinate values of the array.
</p><p>
Integer dimensions coordinates in the output array are changed to start
at zero. (Mapped non-integer coordinates are not changed.) Upper dimension
bounds are adjusted to contain the extent of the data. This means that
each output coordinate axis of subarray are limited to a 62-bit unsigned
integer range.
</p>
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:10,10,0],10*double(random())/2147483647 + 1),A)
subarray(A, 2, 2, 5, 5)

</div><br/>

<a class="op" name="substitute"></a><h2 >substitute</h2>
<i>substitute(array1, array2[, attribute1[, attribute2[, ...]]])</i>
<p/>Create an array that substitutes null values in the indicated attributes in array1 with non-null values from the single-attribute array2.
If no attributes are specified, null values in all attributes will be
substituted.
The single substitute value will be taken from the attribute value of array2 at dimension coordinate zero.
Both array dimension start indices must be zero.
The indicated output array attributes will be set to non-nullable.
<br/><br/>
The example creates an array with two attributes x and y with identical values
that are null below coordinate i=5, then substitutes 0 for the null values in
the y attribute.
<br/>
<div class="x">
store
(
  apply
  (
    build(&lt;x:double null&gt;[i=0:9,10,0],iif(i&lt;5,null,i)),
    y, x
  ),
  A
)
substitute(A, build(&lt;u:double&gt;[j=0:0,1,0],0), y)

</div><br/>



<a class="op" name="unpack"></a><h2 >unpack</h2>
<i>unpack(array, dimension_name[, chunk_size])</i>
<p/>Create a 1-D array version of any array, converting input array dimension values to attributes in the output array. The output array has a single int64 dimension, named according to the dimension_name argument. (The output array resembles
the SciDB "lcsv+" formatted output.)
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:10,10,0], i+j),A)
unpack(A, k)

</div><br/>

<a class="op" name="versions"></a><h2 >versions</h2>
<i>versions(array)</i>
<p/>
Create an array that contains version information for the specified array,
which must be stored in SciDB. The version information includes a sequential
ID and date/time of update.
<br/>
<div class="x">
store(build_sparse(&lt;x:double&gt;[i=1:10,5,0], sqrt(i), double(i)/2 = i/2),A)
versions(A)

insert(build(&lt;x:double&gt;[i=1:10,5,0],i),A)
versions(A)

</div><br/>

<a class="op" name="xgrid"></a><h2 >xgrid</h2>
<i>xgrid(array, scale1[, scale2[, ...]])</i>
<p/>
Create an array that prolongs each dimension of the specified input array
by a scale factor. Input array cells are replicated to fill the new
array. The number of supplied scale arguments must match the number of
dimensions of the array. Each scale argument must be integer-valued 
(type int32).
<br/>
<div class="x">
store(build_sparse(&lt;x:double&gt;[i=1:4,5,0], i, double(i)/2 = i/2),A)
xgrid(A, 2)

</div><br/>
The xgrid operator in some cases can be the inverse of the regrid operator:
<br/><br/>
<div class="x">
store(build_sparse(&lt;x:double&gt;[i=1:4,4,0,j=1:4,4,0], i + j, double(j)/2 = j/2 or double(i)/2 = i/2),A)
xgrid(A, 2, 2)
regrid(xgrid(A,2,2), 2, 2, max(x) as x)

</div>
<br/>






<!-- AGGREGATES -->
<br/> <br/> <br/> <br/>
<h1>SciDB Aggregation functions</h1>
<p>
SciDB <i>aggregates</i> are functions of several values that produce a
scalar value. They are used together with the <i>aggregate</i>,
<i>redimension_store</i>, and
<i>redimension</i> operators to compute data aggregations, optionally
grouped along specified dimensions.
</p>
<hr/>

<a class="ag" name="approxdc"></a><h2>approxdc</h2>
<i>approxdc(attribute values)</i>
<p/>
Approximate distinct count aggregate. The output type will be
unsigned int64, and will be set nullable with a default value of null.
<br/>
<div class="x">
store(build_sparse(&lt;x:double&gt;[i=1:10,5,0], sqrt(i), double(i)/2 = i/2),A)
aggregate(A,approxdc(x) as distinct)

</div><br/>

<a class="ag" name="avg"></a><h2>avg</h2>
<i>avg(attribute values)</i>
<p/>
Average values of aggregated attribute. The input types must
be integer, float or real. The output type will be double
and will be set nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, avg(x) as mean, j)

</div><br/>

<a class="ag" name="count"></a><h2>count</h2>
<i>count(attribute values)</i>
<p/>
Aggregated non-empty count of cells. The output type
will be unsigned int64 and set nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, count(x) as count, j)

</div><br/>

<a class="ag" name="max"></a><h2>max</h2>
<i>max(attribute values)</i>
<p/>
Maximum of aggregated attribute values. The attribute type must
be able to be ordered. The output type is the same as the input
type, but will be nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, max(x) as max, j)

</div><br/>

<a class="ag" name="min"></a><h2>min</h2>
<i>min(attribute values)</i>
<p/>
Minium of aggregated attribute values. The attribute type must
be able to be ordered. The output type is the same as the input
type, but will be nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, min(x) as min, j)

</div><br/>

<a class="ag" name="stdev"></a><h2>stdev</h2>
<i>stdev(attribute values)</i>
<p/>
Standard deviation of the aggregated attribute values. The attribute
type must be integer, float or double. The output type will be double
and will be set nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, stdev(x) as stdev, j)

</div><br/>

<a class="ag" name="sum"></a><h2>sum</h2>
<i>sum(attribute values)</i>
<p/>
Sum the aggregated attribute values. The input attribute
type must be integer, float or double. The output type will be double
for double-valued inputs, float for float-valued inputs, and
int64 for integer inputs, 
and will be set nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, sum(x) as sum, j)

</div><br/>

<a class="ag" name="var"></a><h2>var</h2>
<i>var(attribute values)</i>
<p/>
Variance of the aggregated attribute values. The attribute
type must be integer, float or double. The output type will be double
and will be set nullable with a default value of null.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:5,5,0],i*j), A)
aggregate(A, var(x) as var, j)

</div><br/>

<br/> <br/> <br/> <br/>
<!-- OTHER -->
<h1>Other SciDB AFL commands</h1>
<hr/>
<a class="other" name="cancel"></a><h2 >cancel</h2>
<i>cancel(query_id)</i>
<p/>Cancel a query. Nothing is returned.
<br/>

<a class="op" name="load_library"></a><h2 >load_library</h2>
<i>load_library('plugin_name')</i>
<p/>Load a SciDB plugin.
<br/>
<div class="x">
load_library('dense_linear_algebra')

</div><br/>

<a class="other" name="remove"></a><h2 >remove</h2>
<i>remove(array)</i>
<p/>Remove an array.
<br/>
<div class="x">
store(build(&lt;x:double&gt;[i=1:10,10,0,j=1:10,10,0],i+j),A)
remove(A)

</div><br/>





        </div><!--/span-->
    </div><!--/row-fluid-->

  </div>
<!--   </div>/container -->

  <script src="js/jquery.min.js"></script>
  <script src="js/bootstrap.js"></script>
<script>
window.onload = function()
{
  var a = $(".op");
  var s = "<a href='http://paradigm4.com/documentation/'>This is just a sample of available operators. Full online documentation available from <u>Paradigm4</u></a>";
  s = s + "<br/><br/><ul id='operators' class='nav nav-list'>";
  s = s + "<li class='nav-header'>Operators</li>";
  for(var j=0;j<a.length;j++)
  {
    if(a[j].name.length>0)
      s = s + "<li><a href='#"+a[j].name+"'>"+ a[j].name + "</a></li>";
  }
  a = $(".ag");
  s = s + "<li class='nav-header'>Aggregates</li>";
  for(var j=0;j<a.length;j++)
  {
    if(a[j].name.length>0)
      s = s + "<li><a href='#"+a[j].name+"'>"+ a[j].name + "</a></li>";
  }
  $("#sbar")[0].innerHTML = s;
  a = $(".other");
  s = s + "<li class='nav-header'>Other AFL commands</li>";
  for(var j=0;j<a.length;j++)
  {
    if(a[j].name.length>0)
      s = s + "<li><a href='#"+a[j].name+"'>"+ a[j].name + "</a></li>";
  }
  $("#sbar")[0].innerHTML = s;
}
</script>
</body>
</html>