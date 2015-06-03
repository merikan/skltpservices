#FAQ for SKLTP services



# Release management #

Q: How do i perform a release of a complete domain, e-g crm:scheduling <br>
A: All interactions are wrapped with maven support and a release is done using the following steps<br>
<ol><li>Create a release, step up SNAPSHOT version:<br>
<pre><code>&lt;domain&gt;/trunk/$ mvn release:prepare<br>
</code></pre>
</li><li>We do not perform the actual release. When done with step 1 just clean up using:<br>
<pre><code>&lt;domain&gt;/trunk/$ mvn release:clean<br>
</code></pre>