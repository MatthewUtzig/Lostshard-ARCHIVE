import React from 'react'
import Helmet from 'react-helmet'
import Link from 'gatsby-link'
import get from 'lodash/get'
import { Grid, Container, Span, Breakpoint } from 'react-responsive-grid'

import { rhythm, scale } from '../utils/typography'

class DocumentationTemplate extends React.Component {
  render() {
    const doc = this.props.data.markdownRemark
    const { edges: allDocs } = this.props.data.allMarkdownRemark
    const siteTitle = get(this.props, 'data.site.siteMetadata.title')

    return (
      <div>
      {allDocs
        .filter(_doc => _doc.node.frontmatter.title.length > 0)
        .map(({ node: _doc }) => {
          return (
            <p><Link to={_doc.frontmatter.path}>{_doc.frontmatter.title}</Link></p>
          );
        })}
      <Container
        style={{
          maxWidth: rhythm(24),
          padding: `${rhythm(1.5)} ${rhythm(3 / 4)}`,
        }}
        >
      <div>
        <Helmet title={`${doc.frontmatter.title} | ${siteTitle}`} />
        <h1>{doc.frontmatter.title}</h1>
        <p
          style={{
            ...scale(-1 / 5),
            display: 'block',
            marginBottom: rhythm(1),
            marginTop: rhythm(-1),
          }}
        >
          {doc.frontmatter.date}
        </p>
        <div dangerouslySetInnerHTML={{ __html: doc.html }} />
        <hr
          style={{
            marginBottom: rhythm(1),
          }}
        />
      </div>
      </Container>
      </div>
    )
  }
}

export default DocumentationTemplate

export const pageQuery = graphql`
  query DocumentationByPath($path: String!) {
    site {
      siteMetadata {
        title
        author
      }
    }
    allMarkdownRemark(sort: { order: DESC, fields: [frontmatter___date] }) {
      edges {
        node {
          id
          frontmatter {
            title
            date(formatString: "MMMM DD, YYYY")
            path
          }
        }
      }
    }
    markdownRemark(frontmatter: { path: { eq: $path } }) {
      id
      html
      frontmatter {
        title
        date(formatString: "MMMM DD, YYYY")
      }
    }
  }
`
