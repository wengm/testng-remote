package org.testng.remote.strprotocol;

import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;

import java.util.Collection;
import java.util.List;


/**
 * A <code>IStringMessage</code> implementation for suite running events.
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public class SuiteMessage implements IStringMessage {
  protected final String m_suiteName;
  protected final int m_testMethodCount;
  protected final boolean m_startSuite;
  private List<String> m_excludedMethods = null;

  SuiteMessage(final String suiteName, final boolean startSuiteRun, final int methodCount) {
    m_suiteName = suiteName;
    m_startSuite = startSuiteRun;
    m_testMethodCount = methodCount;
  }

  public SuiteMessage(final ISuite suite, final boolean startSuiteRun) {
    m_suiteName = suite.getName();
    m_testMethodCount =suite.getInvokedMethods().size();
    m_startSuite = startSuiteRun;
    Collection<ITestNGMethod> excludedMethods = suite.getExcludedMethods();
    if (excludedMethods != null && excludedMethods.size() > 0) {
      m_excludedMethods = Lists.newArrayList();
      for (ITestNGMethod m : excludedMethods) {
        m_excludedMethods.add(m.getTestClass().getName() + "." + m.getMethodName());
      }
    }
  }

  public void setExcludedMethods(List<String> methods) {
    m_excludedMethods = Lists.newArrayList();
    m_excludedMethods.addAll(methods);
  }

  public List<String> getExcludedMethods() {
    return m_excludedMethods;
  }

  public boolean isMessageOnStart() {
    return m_startSuite;
  }

  public String getSuiteName() {
    return m_suiteName;
  }

  public int getTestMethodCount() {
    return m_testMethodCount;
  }

  /**
   * @see net.noco.testng.runner.IStringMessage#getMessageAsString()
   */
  @Override
  public String getMessageAsString() {
    StringBuffer buf = new StringBuffer();

    buf.append(m_startSuite ? MessageHelper.SUITE_START : MessageHelper.SUITE_FINISH)
        .append(MessageHelper.DELIMITER)
        .append(m_suiteName)
        .append(MessageHelper.DELIMITER)
        .append(m_testMethodCount)
        ;

    if (m_excludedMethods != null && m_excludedMethods.size() > 0) {
      buf.append(MessageHelper.DELIMITER);
      buf.append(m_excludedMethods.size());
      for (String method : m_excludedMethods) {
        buf.append(MessageHelper.DELIMITER);
        buf.append(method);
      }
    }
    return buf.toString();
  }

  @Override
  public String toString() {
    return "[SuiteMessage suite:" + m_suiteName
        + (m_startSuite ? " starting" : " ending")
        + " methodCount:" + m_testMethodCount
        + "]";
  }

}
